package io.lama06.zombies.system.zombie;

import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieAttributes;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class SpawnFireTrailSystem implements Listener {
    private static final int FIRE_TIME = 4 * 20;

    @EventHandler
    private void onZombieSpawn(final ZombieSpawnEvent event) {
        if (!event.getData().fireTrail) {
            return;
        }
        final Zombie zombie = event.getZombie();
        zombie.set(ZombieAttributes.FIRE_TRAIL, true);
    }

    @EventHandler
    private void onEntityMove(final EntityMoveEvent event) {
        final Zombie zombie = new Zombie(event.getEntity());
        if (!zombie.isZombie()) {
            return;
        }
        final boolean fireTrail = zombie.getOrDefault(ZombieAttributes.FIRE_TRAIL, false);
        if (!fireTrail) {
            return;
        }
        final Block block = zombie.getEntity().getLocation().getBlock();
        if (block.getType() != Material.AIR) {
            return;
        }
        block.setType(Material.FIRE);
        Bukkit.getScheduler().runTaskLater(ZombiesPlugin.INSTANCE, () -> {
            block.setType(Material.AIR);
        }, FIRE_TIME);
    }
}
