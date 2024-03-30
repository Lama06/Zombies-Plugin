package io.lama06.zombies.zombie;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public final class Zombie {
    public static boolean isZombie(final Entity entity) {
        return entity.getPersistentDataContainer().getOrDefault(ZombieAttributes.IS_ZOMBIE.getKey(), PersistentDataType.BOOLEAN, false);
    }

    public static List<Entity> getZombiesInWorld(final World world) {
        final List<Entity> zombies = new ArrayList<>();
        for (final Entity entity : world.getEntities()) {
            if (!Zombie.isZombie(entity)) {
                continue;
            }
            zombies.add(entity);
        }
        return zombies;
    }

    public static List<Entity> getAllZombies() {
        final List<Entity> zombies = new ArrayList<>();
        for (final World world : Bukkit.getWorlds()) {
            zombies.addAll(getZombiesInWorld(world));
        }
        return zombies;
    }
}
