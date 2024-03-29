package io.lama06.zombies.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

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

    @EventHandler
    private void onPlayerDropsItem(final PlayerDropItemEvent event) {
        if (game.getPlayers().containsKey(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onFoodLevelChange(final FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof final Player player && game.getPlayers().containsKey(player)) {
            event.setCancelled(true);
        }
    }
}
