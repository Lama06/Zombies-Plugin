package io.lama06.zombies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.util.json.BlockPositionTypeAdapter;
import io.lama06.zombies.util.json.FinePositionTypeAdapter;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.zombie.Zombie;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public final class ZombiesPlugin extends JavaPlugin implements Listener {
    public static ZombiesPlugin INSTANCE;

    private WorldConfig config = new WorldConfig();

    public WorldConfig getConfig(final ZombiesWorld world) {
        if (world.getBukkit().getName().equals("world")) {
            return config;
        }
        return null;
    }

    public boolean isZombiesWorld(final ZombiesWorld world) {
        return getConfig(world) != null;
    }

    public List<ZombiesWorld> getWorlds() {
        return Bukkit.getWorlds().stream().map(ZombiesWorld::new).filter(ZombiesWorld::isZombiesWorld).toList();
    }

    public List<ZombiesWorld> getGameWorlds() {
        return Bukkit.getWorlds().stream().map(ZombiesWorld::new).filter(ZombiesWorld::isGameRunning).toList();
    }

    public List<ZombiesPlayer> getAlivePlayers() {
        return getGameWorlds().stream().map(ZombiesWorld::getAlivePlayers).flatMap(Collection::stream).toList();
    }

    public List<Weapon> getWeapons() {
        return getAlivePlayers().stream().map(ZombiesPlayer::getWeapons).flatMap(Collection::stream).toList();
    }

    public List<Zombie> getZombies() {
        return getGameWorlds().stream().map(ZombiesWorld::getZombies).flatMap(Collection::stream).toList();
    }

    private Gson createGson() {
        return new GsonBuilder()
                .serializeNulls()
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

        Bukkit.getPluginManager().registerEvents(this, this);
        for (final Listener system : Systems.SYSTEMS) {
            Bukkit.getPluginManager().registerEvents(system, this);
        }

        final PluginCommand zombiesCommand = getCommand("zombies");
        if (zombiesCommand == null) {
            throw new IllegalStateException();
        }
        final ZombiesCommandExecutor zombiesCommandExecutor = new ZombiesCommandExecutor();
        zombiesCommand.setExecutor(zombiesCommandExecutor);
        zombiesCommand.setTabCompleter(zombiesCommandExecutor);
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
}
