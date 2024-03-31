package io.lama06.zombies.zombie.break_window;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.Window;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class BreakWindowSystem implements Listener {
    @EventHandler
    private void spawn(final ZombieSpawnEvent event) {
        final BreakWindowData data = event.getData().breakWindow();
        if (data == null) {
            return;
        }
        final Zombie zombie = event.getZombie();
        final Component breakWindowComponent = zombie.addComponent(ZombieComponents.BREAK_WINDOW);
        breakWindowComponent.set(BreakWindowAttributes.TIME, data.time());
        breakWindowComponent.set(BreakWindowAttributes.MAX_DISTANCE, data.maxDistance());
    }

    @EventHandler
    private void start(final ServerTickEndEvent event) {
        for (final Zombie zombie : ZombiesPlugin.INSTANCE.getZombies()) {
            startZombie(zombie);
        }
    }

    private void startZombie(final Zombie zombie) {
        final Component breakWindowComponent = zombie.getComponent(ZombieComponents.BREAK_WINDOW);
        if (breakWindowComponent == null) {
            return;
        }
        final int time = breakWindowComponent.get(BreakWindowAttributes.TIME);
        final double maxDistance = breakWindowComponent.get(BreakWindowAttributes.MAX_DISTANCE);
        final BlockPosition block = breakWindowComponent.getOrDefault(BreakWindowAttributes.BLOCK, null);
        if (block != null) {
            return;
        }
        final NearestWindowBlockResult nearestWindowBlock = getNearestWindowBlock(zombie);
        if (nearestWindowBlock == null || nearestWindowBlock.distance() > maxDistance) {
            return;
        }
        breakWindowComponent.set(BreakWindowAttributes.BLOCK, nearestWindowBlock.position());
        breakWindowComponent.set(BreakWindowAttributes.REMAINING_TIME, time);
    }

    private record NearestWindowBlockResult(BlockPosition position, double distance) { }

    private @Nullable NearestWindowBlockResult getNearestWindowBlock(final Zombie zombie) {
        final ZombiesWorld world = zombie.getWorld();
        BlockPosition nearestWindowBlock = null;
        double smallestDistance = Double.POSITIVE_INFINITY;
        for (final Window window : ZombiesPlugin.getConfig(world).windows) {
            for (final BlockPosition windowBlockPos : window.blocks.getBlocks()) {
                final Block block = windowBlockPos.toLocation(world.getBukkit()).getBlock();
                if (block.getType() == Material.AIR) {
                    continue;
                }
                final Location zombieLocation = zombie.getEntity() instanceof final LivingEntity living
                        ? living.getEyeLocation()
                        : zombie.getEntity().getLocation();
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
        for (final Zombie zombie : ZombiesPlugin.INSTANCE.getZombies()) {
            tickZombie(zombie);
        }
    }

    private void tickZombie(final Zombie zombie) {
        final Component breakWindowComponent = zombie.getComponent(ZombieComponents.BREAK_WINDOW);
        if (breakWindowComponent == null) {
            return;
        }
        final Integer remainingTime = breakWindowComponent.getOrDefault(BreakWindowAttributes.REMAINING_TIME, null);
        final BlockPosition block = breakWindowComponent.getOrDefault(BreakWindowAttributes.BLOCK, null);
        if (remainingTime == null || block == null) {
            return;
        }
        if (remainingTime == 1) {
            breakWindowComponent.remove(BreakWindowAttributes.REMAINING_TIME);
            breakWindowComponent.remove(BreakWindowAttributes.BLOCK);
            block.toLocation(zombie.getWorld().getBukkit()).getBlock().setType(Material.AIR);
            return;
        }
        breakWindowComponent.set(BreakWindowAttributes.REMAINING_TIME, remainingTime - 1);
        if (!new BreakWindowTickEvent(zombie, remainingTime - 1).callEvent()) {
            breakWindowComponent.remove(BreakWindowAttributes.REMAINING_TIME);
            breakWindowComponent.remove(BreakWindowAttributes.BLOCK);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void cancelMoveWhileBreakingWindow(final EntityMoveEvent event) {
        final Entity entity = event.getEntity();
        if (!Zombie.isZombie(entity)) {
            return;
        }
        final Zombie zombie = new Zombie(entity);
        final Component breakWindowComponent = zombie.getComponent(ZombieComponents.BREAK_WINDOW);
        if (breakWindowComponent == null) {
            return;
        }
        final Integer remainingTime = breakWindowComponent.getOrDefault(BreakWindowAttributes.REMAINING_TIME, null);
        if (remainingTime == null) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    private void cancelWhenBlockDisappears(final BreakWindowTickEvent event) {
        final Zombie zombie = event.getZombie();
        final Component breakWindowComponent = zombie.getComponent(ZombieComponents.BREAK_WINDOW);
        Objects.requireNonNull(breakWindowComponent);
        final BlockPosition blockPos = breakWindowComponent.getOrDefault(BreakWindowAttributes.BLOCK, null);
        if (blockPos == null) {
            return;
        }
        final Block block = blockPos.toLocation(event.getWorld().getBukkit()).getBlock();
        if (block.getType() == Material.AIR) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void playSound(final BreakWindowTickEvent event) {
        final int SOUND_DELAY = 2 * 20;
        final Zombie zombie = event.getZombie();
        if (event.getNewRemainingTime() % SOUND_DELAY != 0) {
            return;
        }
        zombie.getWorld().getBukkit().playSound(
                zombie.getEntity(),
                Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR,
                SoundCategory.HOSTILE,
                1,
                1
        );
    }
}
