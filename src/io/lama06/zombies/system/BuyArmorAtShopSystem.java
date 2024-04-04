package io.lama06.zombies.system;

import io.lama06.zombies.ArmorShop;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.ZombiesPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class BuyArmorAtShopSystem implements Listener {
    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
            return;
        }
        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final ZombiesWorld world = player.getWorld();
        if (!world.isGameRunning()) {
            return;
        }
        final ArmorShop armorShop = world.getConfig().armorShops.stream()
                .filter(shop -> shop.position.equals(clickedBlock.getLocation().toBlock()))
                .findAny().orElse(null);
        if (armorShop == null) {
            return;
        }
        final int gold = player.get(ZombiesPlayer.GOLD);
        if (gold < armorShop.price) {
            player.sendMessage(Component.text("You cannot afford this").color(NamedTextColor.RED));
            return;
        }
        final PlayerInventory inventory = player.getBukkit().getInventory();
        for (final EquipmentSlot equipmentSlot : armorShop.part.getEquipmentSlots()) {
            final Material armorMaterial = armorShop.quality.materials.get(equipmentSlot);
            final Material playerMaterial = inventory.getItem(equipmentSlot).getType();
            if (playerMaterial == armorMaterial) {
                player.sendMessage(Component.text("You already own this").color(NamedTextColor.RED));
                return;
            }
        }

        final int newGold = gold - armorShop.price;
        player.set(ZombiesPlayer.GOLD, newGold);
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, gold, newGold));
        for (final EquipmentSlot equipmentSlot : armorShop.part.getEquipmentSlots()) {
            final ItemStack item = new ItemStack(armorShop.quality.materials.get(equipmentSlot));
            inventory.setItem(equipmentSlot, item);
        }
        player.sendMessage(Component.text("Successfully bought the armor").color(NamedTextColor.GREEN));
    }
}
