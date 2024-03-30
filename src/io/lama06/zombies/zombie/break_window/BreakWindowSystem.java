package io.lama06.zombies.zombie.break_window;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.Window;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.util.pdc.BlockPositionPersistentDataType;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieAttributes;
import io.lama06.zombies.zombie.event.ZombieSpawnEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public final class BreakWindowSystem implements Listener {
    @EventHandler
    private void spawn(final ZombieSpawnEvent event) {
        final BreakWindowData data = event.getData().breakWindow();
        if (data == null) {
            return;
        }
        final PersistentDataContainer pdc = event.getZombie().getPersistentDataContainer();
        final PersistentDataContainer container = pdc.getAdapterContext().newPersistentDataContainer();
        container.set(BreakWindowAttributes.TIME.getKey(), PersistentDataType.INTEGER, data.time());
        container.set(BreakWindowAttributes.MAX_DISTANCE.getKey(), PersistentDataType.DOUBLE, data.maxDistance());
        pdc.set(ZombieAttributes.BREAK_WINDOW.getKey(), PersistentDataType.TAG_CONTAINER, container);
    }

    @EventHandler
    private void start(final ServerTickEndEvent event) {
        for (final Entity zombie : Zombie.getAllZombies()) {
            startZombie(zombie);
        }
    }

    private void startZombie(final Entity zombie) {
        final PersistentDataContainer pdc = zombie.getPersistentDataContainer();
        final PersistentDataContainer breakWindowContainer = pdc.get(ZombieAttributes.BREAK_WINDOW.getKey(), PersistentDataType.TAG_CONTAINER);
        if (breakWindowContainer == null) {
            return;
        }
        final Integer time = breakWindowContainer.get(BreakWindowAttributes.TIME.getKey(), PersistentDataType.INTEGER);
        final Integer remainingTime = breakWindowContainer.get(BreakWindowAttributes.REMAINING_TIME.getKey(), PersistentDataType.INTEGER);
        final Double maxDistance = breakWindowContainer.get(BreakWindowAttributes.MAX_DISTANCE.getKey(), PersistentDataType.DOUBLE);
        if (time == null || remainingTime != null || maxDistance == null) {
            return;
        }
        final NearestWindowBlockResult nearestWindowBlock = getNearestWindowBlock(zombie);
        if (nearestWindowBlock == null || nearestWindowBlock.distance() > maxDistance) {
            return;
        }
        breakWindowContainer.set(BreakWindowAttributes.REMAINING_TIME.getKey(), PersistentDataType.INTEGER, time);
        breakWindowContainer.set(BreakWindowAttributes.BLOCK.getKey(), BlockPositionPersistentDataType.INSTANCE, nearestWindowBlock.position());
        pdc.set(ZombieAttributes.BREAK_WINDOW.getKey(), PersistentDataType.TAG_CONTAINER, breakWindowContainer);
    }

    private record NearestWindowBlockResult(BlockPosition position, double distance) { }

    private @Nullable NearestWindowBlockResult getNearestWindowBlock(final Entity zombie) {
        final World world = zombie.getWorld();
        BlockPosition nearestWindowBlock = null;
        double smallestDistance = Double.POSITIVE_INFINITY;
        for (final Window window : ZombiesPlugin.getConfig(world).windows) {
            for (final BlockPosition windowBlockPos : window.blocks.getBlocks()) {
                final Block block = windowBlockPos.toLocation(world).getBlock();
                if (block.getType() == Material.AIR) {
                    continue;
                }
                final Location zombieLocation = zombie instanceof final LivingEntity living ? living.getEyeLocation() : zombie.getLocation();
                final double distance = windowBlockPos.toCenter().toVector().distance(zombieLocation.toVector());
                if (distance < smallestDistance) {
                    nearestWindowBlock = windowBlockPos;
                    smallestDistance = distance;
                }
            }
        }
        if (nearestWindowBlock == null) {
            return null;
        }
        return new NearestWindowBlockResult(nearestWindowBlock, smallestDistance);
    }

    @EventHandler
    private void tick(final ServerTickEndEvent event) {
        for (final Entity zombie : Zombie.getAllZombies()) {
            tickZombie(zombie);
        }
    }

    private void tickZombie(final Entity zombie) {
        final PersistentDataContainer pdc = zombie.getPersistentDataContainer();
        final PersistentDataContainer container = pdc.get(ZombieAttributes.BREAK_WINDOW.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final Integer time = container.get(BreakWindowAttributes.TIME.getKey(), PersistentDataType.INTEGER);
        final Integer remainingTime = container.get(BreakWindowAttributes.REMAINING_TIME.getKey(), PersistentDataType.INTEGER);
        final BlockPosition blockPos = container.get(BreakWindowAttributes.BLOCK.getKey(), BlockPositionPersistentDataType.INSTANCE);
        if (time == null || remainingTime == null || blockPos == null) {
            return;
        }
        if (remainingTime == 0) {
            return;
        }
        if (remainingTime == 1) {
            container.remove(BreakWindowAttributes.REMAINING_TIME.getKey());
            container.remove(BreakWindowAttributes.BLOCK.getKey());
            pdc.set(ZombieAttributes.BREAK_WINDOW.getKey(), PersistentDataType.TAG_CONTAINER, container);
            blockPos.toLocation(zombie.getWorld()).getBlock().setType(Material.AIR);
            return;
        }
        container.set(BreakWindowAttributes.REMAINING_TIME.getKey(), PersistentDataType.INTEGER, remainingTime - 1);
        pdc.set(ZombieAttributes.BREAK_WINDOW.getKey(), PersistentDataType.TAG_CONTAINER, container);
        if (!new BreakWindowTickEvent(zombie, remainingTime - 1).callEvent()) {
            container.remove(BreakWindowAttributes.REMAINING_TIME.getKey());
            container.remove(BreakWindowAttributes.BLOCK.getKey());
            pdc.set(ZombieAttributes.BREAK_WINDOW.getKey(), PersistentDataType.TAG_CONTAINER, container);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void cancelMoveWhileBreakingWindow(final EntityMoveEvent event) {
        final Entity zombie = event.getEntity();
        if (!Zombie.isZombie(zombie)) {
            return;
        }
        final PersistentDataContainer pdc = zombie.getPersistentDataContainer();
        final PersistentDataContainer container = pdc.get(ZombieAttributes.BREAK_WINDOW.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final Integer remainingTime = container.get(BreakWindowAttributes.REMAINING_TIME.getKey(), PersistentDataType.INTEGER);
        if (remainingTime == null) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    private void cancelWhenBlockDisappears(final BreakWindowTickEvent event) {
        final Entity zombie = event.getZombie();
        final PersistentDataContainer pdc = zombie.getPersistentDataContainer();
        final PersistentDataContainer container = pdc.get(ZombieAttributes.BREAK_WINDOW.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final BlockPosition blockPosition = container.get(BreakWindowAttributes.BLOCK.getKey(), BlockPositionPersistentDataType.INSTANCE);
        if (blockPosition == null) {
            return;
        }
        final Block block = blockPosition.toLocation(event.getWorld()).getBlock();
        if (block.getType() == Material.AIR) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void playSound(final BreakWindowTickEvent event) {
        final int SOUND_DELAY = 2 * 20;
        final Entity zombie = event.getZombie();
        if (event.getNewRemainingTime() % SOUND_DELAY != 0) {
            return;
        }
        zombie.getWorld().playSound(zombie, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1, 1);
    }
}
