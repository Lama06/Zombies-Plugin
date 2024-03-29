package io.lama06.zombies.zombie.break_window.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.System;
import io.lama06.zombies.Window;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.break_window.BreakWindowComponent;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.Nullable;

public final class BreakWindowStartSystem extends System {
    public BreakWindowStartSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final Zombie zombie : game.getZombies().values()) {
            tickZombie(zombie);
        }
    }

    private void tickZombie(final Zombie zombie) {
        final BreakWindowComponent breakWindow = zombie.getBreakWindow();
        if (breakWindow == null) {
            return;
        }
        if (breakWindow.isBreakingWindow()) {
            return;
        }
        final NearestWindowBlockResult nearestWindowBlock = getNearestWindowBlock(zombie);
        if (nearestWindowBlock == null || nearestWindowBlock.distance() > breakWindow.getMaxDistance()) {
            return;
        }
        breakWindow.start(nearestWindowBlock.position());
    }

    private record NearestWindowBlockResult(BlockPosition position, double distance) { }

    private @Nullable NearestWindowBlockResult getNearestWindowBlock(final Zombie zombie) {
        BlockPosition nearestWindowBlock = null;
        double smallestDistance = Double.POSITIVE_INFINITY;
        for (final Window window : game.getConfig().windows) {
            for (final BlockPosition windowBlockPos : window.blocks.getBlocks()) {
                final Block block = windowBlockPos.toLocation(game.getWorld()).getBlock();
                if (block.getType() == Material.AIR) {
                    continue;
                }
                final double distance = windowBlockPos.toCenter().toVector().distance(zombie.getEntity().getLocation().toVector());
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
