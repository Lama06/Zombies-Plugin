package io.lama06.zombies.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public final class PreventEventsSystem extends System {
    public PreventEventsSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onPlayerBreakBlockEvent(final BlockBreakEvent event) {
        if (event.getBlock().getWorld().equals(game.getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerDamageEntityEvent(final PrePlayerAttackEntityEvent event) {
        if (event.getPlayer().getWorld().equals(game.getWorld())) {
            event.setCancelled(true);
        }
    }
}
