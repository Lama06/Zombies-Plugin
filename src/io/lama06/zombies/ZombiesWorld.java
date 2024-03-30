package io.lama06.zombies;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class ZombiesWorld {
    public static boolean isGameWorld(final World world) {
        return world.getPersistentDataContainer().getOrDefault(WorldAttributes.IS_GAME.getKey(), PersistentDataType.BOOLEAN, false);
    }

    public static List<World> getGameWorlds() {
        return Bukkit.getWorlds().stream().filter(ZombiesWorld::isGameWorld).toList();
    }
}
