package io.lama06.zombies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lama06.zombies.util.json.BlockPositionTypeAdapter;
import io.lama06.zombies.util.json.FinePositionTypeAdapter;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ZombiesPlugin extends JavaPlugin {
    public static ZombiesPlugin INSTANCE;

    private WorldConfig config = new WorldConfig();
    private ZombiesGame game;

    private Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BlockPosition.class, new BlockPositionTypeAdapter())
                .registerTypeAdapter(FinePosition.class, new FinePositionTypeAdapter())
                .create();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        final Path data = getDataFolder().toPath();

        try {
            if (!Files.exists(data)) Files.createDirectory(data);
            if (!Files.exists(data.resolve("data.json"))) {
                Files.createFile(data.resolve("data.json"));
                return;
            }
            final String configText = Files.readString(data.resolve("data.json"));
            final Gson gson = createGson();
            config = gson.fromJson(configText, WorldConfig.class);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        final Path data = getDataFolder().toPath();
        try {
            final Gson gson = createGson();
            final String configText = gson.toJson(config);
            Files.writeString(data.resolve("data.json"), configText);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final @NotNull String[] args
    ) {
        if (args.length == 0) return false;
        if (!(sender instanceof final Player player)) return false;
        switch (args[0]) {
            case "config" -> {
                config.openMenu(player, () -> {});
            }
            case "start" -> {
                try {
                    config.check();
                } catch (final InvalidConfigException e) {
                    player.sendMessage(Component.text(e.getLocalizedMessage()));
                    return true;
                }
                game = new ZombiesGame(player.getWorld(), config);
            }
        }
        return true;
    }
}
