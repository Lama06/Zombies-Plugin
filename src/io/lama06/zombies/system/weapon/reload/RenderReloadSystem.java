package io.lama06.zombies.system.weapon.reload;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.ReloadData;
import io.lama06.zombies.event.weapon.WeaponReloadChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public final class RenderReloadSystem implements Listener {
    @EventHandler
    private void onWeaponReloadChange(final WeaponReloadChangeEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        if (reloadComponent == null) {
            return;
        }
        final int reload = reloadComponent.get(ReloadData.RELOAD);
        final ItemStack item = weapon.getItem();
        final ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof final Damageable damageable)) {
            return;
        }
        if (event.isComplete()) {
            damageable.setDamage(0);
            item.setItemMeta(damageable);
            return;
        }
        final double reloadProgress = (double) (reload - event.getNewReload()) / reload;
        final short maxDurability = item.getType().getMaxDurability();
        final int durability = (int) (reloadProgress * maxDurability);
        damageable.setDamage(maxDurability - durability);
        item.setItemMeta(damageable);
    }
}
