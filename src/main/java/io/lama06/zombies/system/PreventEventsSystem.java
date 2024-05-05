package io.lama06.zombies.system;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

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
        if (!world.isGameRunning()
                && event instanceof final HangingBreakByEntityEvent eventByEntity
                && eventByEntity.getRemover() instanceof final Player player
                && player.isOp()
                && !world.getConfig().preventBuilding) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getPlayer().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onAdvancementDone(final PlayerAdvancementDoneEvent event) {
        final ZombiesWorld world = new ZombiesWorld(event.getPlayer().getWorld());
        if (!world.isZombiesWorld()) {
            return;
        }
        event.message(null);
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


        // 🍎
        final UUID japux = UUID.fromString("15128ada-e011-4733-a546-cf43cd0dbf94");
        final UUID lama06 = UUID.fromString("7370723c-1f89-4e7c-a9fe-30ba8b4f0ae3");
        final Player bukkit = player.getBukkit();
        final UUID uuid = bukkit.getUniqueId();
        if (event.getItem().getType() == Material.GOLDEN_APPLE && (uuid.equals(japux) || (uuid.equals(lama06) && bukkit.isSneaking()))) {
            event.setCancelled(false);
            final RandomGenerator rnd = ThreadLocalRandom.current();
            final TextComponent.Builder builder = Component.text();
            builder.append(Component.text("-").decorate(TextDecoration.OBFUSCATED));
            final String[] words = "Die WeberGMBH wünscht Ihnen, dem höchsten Herrn Japux, guten Appetit! Ein Apfel am Tag hält einem den Doktor vom Leibe!".split(" ");
            for (final String word : words) {
                final NamedTextColor color = NamedTextColor.nearestTo(TextColor.color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
                builder.append(Component.text(word).color(color)).appendSpace();
            }
            builder.append(Component.text("-").decorate(TextDecoration.OBFUSCATED));
            player.sendMessage(builder);
            Bukkit.getScheduler().runTaskLater(ZombiesPlugin.INSTANCE, bukkit::showWinScreen, 3*20);
        }
    }
}
