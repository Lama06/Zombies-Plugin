package io.lama06.zombies.system;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.ZombiesWorld;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public final class PreventEventsSystem implements Listener {
    @EventHandler
    private void onPlayerBreakBlock(final BlockBreakEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getPlayer().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        if (!world.isGameRunning() && event.getPlayer().isOp() && !world.getConfig().preventBuilding) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onHangingBreak(final HangingBreakEvent event) { // protect paintings
        final ZombiesWorld world = new ZombiesWorld(event.getEntity().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        if (event instanceof final HangingBreakByEntityEvent eventByEntity
                && eventByEntity.getRemover() instanceof Player
                && !world.getConfig().preventBuilding) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockPlace(final BlockPlaceEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getPlayer().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        if (!world.isGameRunning() && event.getPlayer().isOp() && !world.getConfig().preventBuilding) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDamageEntity(final PrePlayerAttackEntityEvent event) {
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

    @EventHandler
    private void onBlockBurn(final BlockBurnEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getBlock().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockIgnite(final BlockIgniteEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getBlock().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onEntityExplode(final EntityExplodeEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getEntity().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryClick(final InventoryClickEvent event) {
        final ZombiesPlayer player = new ZombiesPlayer((Player) event.getWhoClicked());
        if (!player.getWorld().isGameRunning()) {
            return;
        }
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(player.getBukkit().getInventory())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerItemConsume(final PlayerItemConsumeEvent event) {
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        if (!player.getWorld().isGameRunning()) {
            return;
        }
        event.setCancelled(true);
    }
}
