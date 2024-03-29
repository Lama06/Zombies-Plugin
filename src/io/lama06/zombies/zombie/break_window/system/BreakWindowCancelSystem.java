package io.lama06.zombies.zombie.break_window.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.break_window.event.BreakWindowTickEvent;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public final class BreakWindowCancelSystem extends System {
    public BreakWindowCancelSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onTick(final BreakWindowTickEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        final BlockPosition blockPosition = event.getZombie().getBreakWindow().getWindowBlock();
        final Block block = blockPosition.toLocation(game.getWorld()).getBlock();
        if (block.getType() == Material.AIR) {
            event.setCancelled(true);
        }
    }
}
