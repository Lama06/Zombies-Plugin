package io.lama06.zombies.weapon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public record Weapon(Player owner, int slot) {
    public static boolean isWeapon(final ItemStack weapon) {
        final ItemMeta meta = weapon.getItemMeta();
        if (meta == null) {
            return false;
        }
        return meta.getPersistentDataContainer().getOrDefault(WeaponAttributes.IS_WEAPON.getKey(), PersistentDataType.BOOLEAN, false);
    }

    public static Weapon getHeldWeapon(final Player player) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack item = inventory.getItemInMainHand();
        if (!isWeapon(item)) {
            return null;
        }
        return new Weapon(player, inventory.getHeldItemSlot());
    }

    public static List<Weapon> getPlayerWeapons(final Player player) {
        final PlayerInventory inventory = player.getInventory();
        final List<Weapon> weapons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final ItemStack item = inventory.getItem(i);
            if (item == null || !isWeapon(item)) {
                continue;
            }
            weapons.add(new Weapon(player, i));
        }
        return weapons;
    }

    public static List<Weapon> getAllWeapons() {
        final List<Weapon> weapons = new ArrayList<>();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            weapons.addAll(getPlayerWeapons(player));
        }
        return weapons;
    }

    public ItemStack getItem() {
        return owner.getInventory().getItem(slot);
    }
}
