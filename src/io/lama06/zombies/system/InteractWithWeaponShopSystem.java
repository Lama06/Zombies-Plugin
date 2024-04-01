package io.lama06.zombies.system;

import io.lama06.zombies.WeaponShop;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.event.weapon.WeaponAmmoChangeEvent;
import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.AmmoAttributes;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponAttributes;
import io.lama06.zombies.weapon.WeaponComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class InteractWithWeaponShopSystem implements Listener {
    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
            return;
        }
        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        final Player bukkit = event.getPlayer();
        if (!ZombiesPlayer.isZombiesPlayer(bukkit)) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(bukkit);
        final WeaponShop weaponShop = ZombiesPlugin.getConfig(player.getWorld()).weaponShops.stream()
                .filter(shop -> shop.position.equals(clickedBlock.getLocation().toBlock()))
                .findAny()
                .orElse(null);
        if (weaponShop == null) {
            return;
        }
        final Weapon heldWeapon = player.getHeldWeapon();
        if (heldWeapon == null || !heldWeapon.get(WeaponAttributes.TYPE).equals(weaponShop.weaponType)) {
            buyWeapon(player, weaponShop);
        } else {
            refillWeapon(player, weaponShop, heldWeapon);
        }
    }

    private void buyWeapon(final ZombiesPlayer player, final WeaponShop shop) {
        final int gold = player.get(PlayerAttributes.GOLD);
        if (gold < shop.purchasePrice) {
            player.sendMessage(Component.text("You cannot afford this weapon").color(NamedTextColor.RED));
            return;
        }
        final int slot = player.getBukkit().getInventory().getHeldItemSlot();
        if (slot >= 3) {
            player.sendMessage(Component.text("Switch to a weapon slot").color(NamedTextColor.RED));
            return;
        }
        player.giveWeapon(slot, shop.weaponType);
        player.set(PlayerAttributes.GOLD, gold - shop.purchasePrice);
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, gold, gold - shop.purchasePrice));
        player.sendMessage(Component.text("Successfully bought the weapon").color(NamedTextColor.GREEN));
    }

    private void refillWeapon(final ZombiesPlayer player, final WeaponShop shop, final Weapon weapon) {
        final int gold = player.get(PlayerAttributes.GOLD);
        if (gold < shop.refillPrice) {
            player.sendMessage(Component.text("You cannot afford refilling this weapon").color(NamedTextColor.RED));
            return;
        }
        final io.lama06.zombies.data.Component ammComponent = weapon.getComponent(WeaponComponents.AMMO);
        if (ammComponent == null) {
            player.sendMessage(Component.text("This weapon cannot be refilled").color(NamedTextColor.RED));
            return;
        }
        final int clip = ammComponent.get(AmmoAttributes.CLIP);
        final int ammo = ammComponent.get(AmmoAttributes.AMMO);
        final int maxAmmo = ammComponent.get(AmmoAttributes.MAX_AMMO);
        final int maxClip = ammComponent.get(AmmoAttributes.MAX_CLIP);
        if (ammo == maxAmmo) {
            player.sendMessage(Component.text("This weapon is already refilled").color(NamedTextColor.RED));
            return;
        }
        ammComponent.set(AmmoAttributes.CLIP, maxClip);
        ammComponent.set(AmmoAttributes.AMMO, maxAmmo);
        Bukkit.getPluginManager().callEvent(new WeaponAmmoChangeEvent(weapon, ammo, maxAmmo));
        Bukkit.getPluginManager().callEvent(new WeaponClipChangeEvent(weapon, clip, maxClip));

        player.set(PlayerAttributes.GOLD, gold - shop.refillPrice);
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, gold, gold - shop.refillPrice));

        player.sendMessage(Component.text("Successfully refilled the weapon").color(NamedTextColor.GREEN));
    }
}
