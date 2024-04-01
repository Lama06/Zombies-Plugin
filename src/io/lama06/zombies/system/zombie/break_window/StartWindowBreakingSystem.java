package io.lama06.zombies.system.zombie.break_window;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.Window;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.BreakWindowAttributes;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

public final class StartWindowBreakingSystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
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
}