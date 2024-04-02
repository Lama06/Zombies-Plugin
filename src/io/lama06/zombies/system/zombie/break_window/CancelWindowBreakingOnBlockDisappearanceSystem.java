package io.lama06.zombies.system.zombie.break_window;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.zombie.BreakWindowTickEvent;
import io.lama06.zombies.zombie.BreakWindowData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public final class CancelWindowBreakingOnBlockDisappearanceSystem implements Listener {
    @EventHandler(ignoreCancelled = true)
    private void onBreakWindowTick(final BreakWindowTickEvent event) {
        final Zombie zombie = event.getZombie();
        final Component breakWindowComponent = zombie.getComponent(ZombieComponents.BREAK_WINDOW);
        Objects.requireNonNull(breakWindowComponent);
        final BlockPosition blockPos = breakWindowComponent.getOrDefault(BreakWindowData.BLOCK, null);
        if (blockPos == null) {
            return;
        }
        final Block block = blockPos.toLocation(event.getWorld().getBukkit()).getBlock();
        if (block.getType() == Material.AIR) {
            event.setCancelled(true);
        }
    }
}
