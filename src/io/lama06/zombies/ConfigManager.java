package io.lama06.zombies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lama06.zombies.util.json.BlockPositionTypeAdapter;
import io.lama06.zombies.util.json.FinePositionTypeAdapter;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public final class ConfigManager {
    private static final String CONFIG_FILE_NAME = "data.json";
    private static final String BACKUP_DIRECTORY_NAME = "backups";

    private final ZombiesPlugin plugin;

    public ConfigManager(final ZombiesPlugin plugin) {
        this.plugin = plugin;
    }

    private Gson createGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(BlockPosition.class, new BlockPositionTypeAdapter())
                .registerTypeAdapter(FinePosition.class, new FinePositionTypeAdapter())
                .create();
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

    public ZombiesConfig loadConfig() throws IOException {
        final Path dataDirectory = getDataDirectoryPath();
        final Path configFilePath = dataDirectory.resolve(CONFIG_FILE_NAME);
        if (!Files.exists(configFilePath)) {
            return new ZombiesConfig();
        }
        final String configText = Files.readString(configFilePath);
        final Gson gson = createGson();
        return gson.fromJson(configText, ZombiesConfig.class);
    }

    public void saveConfig(final ZombiesConfig config) throws IOException {
        final Path dataDirectory = getDataDirectoryPath();
        final Path configFilePath = dataDirectory.resolve(CONFIG_FILE_NAME);
        if (!Files.exists(configFilePath)) {
            Files.createFile(configFilePath);
        }
        final Gson gson = createGson();
        final String configText = gson.toJson(config);
        Files.writeString(configFilePath, configText);
    }
}
