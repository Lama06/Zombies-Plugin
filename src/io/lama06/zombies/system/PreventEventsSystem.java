package io.lama06.zombies.system;

import io.lama06.zombies.ZombiesWorld;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public final class PreventEventsSystem implements Listener {
    @EventHandler
    private void onPlayerBreakBlockEvent(final BlockBreakEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getPlayer().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockPlaceEvent(final BlockPlaceEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getPlayer().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDamageEntityEvent(final PrePlayerAttackEntityEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getPlayer().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDropsItem(final PlayerDropItemEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getPlayer().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onFoodLevelChange(final FoodLevelChangeEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getEntity().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onEntityDeath(final EntityDeathEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getEntity().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setDroppedExp(0);
        event.getDrops().clear();
    }
}
