package io.lama06.zombies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.lama06.zombies.util.json.BlockPositionTypeAdapter;
import io.lama06.zombies.util.json.FinePositionTypeAdapter;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public final class ConfigManager {
    private static final String CONFIG_FILE_NAME = "data.json";
    private static final String BACKUP_DIRECTORY_NAME = "backups";
    private static final Duration CONFIG_AUTO_SAVE_DELAY = Duration.ofMinutes(20);

    public static Gson createGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(BlockPosition.class, new BlockPositionTypeAdapter())
                .registerTypeAdapter(FinePosition.class, new FinePositionTypeAdapter())
                .create();
    }

    private final ZombiesPlugin plugin;
    private ZombiesConfig config;

    public ConfigManager(final ZombiesPlugin plugin) {
        this.plugin = plugin;
        startAutoSaveConfigTask();
    }

    private Path getDataDirectoryPath() throws IOException {
        final Path dataFolderPath = plugin.getDataFolder().toPath();
        if (!Files.exists(dataFolderPath)) {
            Files.createDirectory(dataFolderPath);
        }
        return dataFolderPath;
    }

    private Path getBackupDirectory() throws IOException {
        final Path dataDirectoryPath = getDataDirectoryPath();
        final Path backupDirectory = dataDirectoryPath.resolve(BACKUP_DIRECTORY_NAME);
        if (!Files.exists(backupDirectory)) {
            Files.createDirectory(backupDirectory);
        }
        return backupDirectory;
    }

    private String getBackupFileName() {
        final LocalDateTime now = LocalDateTime.now();
        return "%d-%d-%d-%d-%d-%d.json".formatted(
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                now.getSecond()
        );
    }

    public void backupConfig() throws IOException {
        final Path dataDirectory = getDataDirectoryPath();
        final Path configFilePath = dataDirectory.resolve(CONFIG_FILE_NAME);
        if (!Files.exists(configFilePath)) {
            return;
        }
        final String configText = Files.readString(configFilePath);

        final Path backupDirectory = getBackupDirectory();
        final Path backupFilePath = backupDirectory.resolve(getBackupFileName());
        if (Files.exists(backupFilePath)) {
            return;
        }
        Files.createFile(backupFilePath);
        Files.writeString(backupFilePath, configText);
    }

    public void loadConfig() throws IOException {
        final Path dataDirectory = getDataDirectoryPath();
        final Path configFilePath = dataDirectory.resolve(CONFIG_FILE_NAME);
        if (!Files.exists(configFilePath)) {
            config = new ZombiesConfig();
            return;
        }
        final String configText = Files.readString(configFilePath);
        final Gson gson = createGson();
        try {
            config = gson.fromJson(configText, ZombiesConfig.class);
        } catch (final JsonSyntaxException e) {
            throw new IOException("invalid json", e);
        }
    }

    public void saveConfig() throws IOException {
        final Path dataDirectory = getDataDirectoryPath();
        final Path configFilePath = dataDirectory.resolve(CONFIG_FILE_NAME);
        if (!Files.exists(configFilePath)) {
            Files.createFile(configFilePath);
        }
        final Gson gson = createGson();
        final String configText = gson.toJson(plugin.getGlobalConfig());
        Files.writeString(configFilePath, configText);
    }

    private void startAutoSaveConfigTask() {
        final long delayTicks = CONFIG_AUTO_SAVE_DELAY.getSeconds() * 20;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            try {
                saveConfig();
            } catch (final IOException e) {
                plugin.getSLF4JLogger().error("Failed to automatically save the config. Please create a backup yourself to avoid data loss.", e);
            }
        }, delayTicks, delayTicks);
    }

    public ZombiesConfig getConfig() {
        return config;
    }
}
