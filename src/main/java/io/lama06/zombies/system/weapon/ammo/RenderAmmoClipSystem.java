package io.lama06.zombies.system.weapon.ammo;

import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public final class RenderAmmoClipSystem implements Listener {
    @EventHandler
    private void onWeaponClipChange(final WeaponClipChangeEvent event) {
        final ItemStack item = event.getWeapon().getItem();
        item.setAmount(Math.max(1, event.getNewClip()));
    }
}
