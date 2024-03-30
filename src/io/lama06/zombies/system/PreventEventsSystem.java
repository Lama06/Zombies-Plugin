package io.lama06.zombies.system;

import io.lama06.zombies.ZombiesWorld;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public final class PreventEventsSystem implements Listener {
    @EventHandler
    private void onPlayerBreakBlockEvent(final BlockBreakEvent event) {
        if (!ZombiesWorld.isGameWorld(event.getPlayer().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDamageEntityEvent(final PrePlayerAttackEntityEvent event) {
        if (!ZombiesWorld.isGameWorld(event.getPlayer().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDropsItem(final PlayerDropItemEvent event) {
        if (!ZombiesWorld.isGameWorld(event.getPlayer().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onFoodLevelChange(final FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
