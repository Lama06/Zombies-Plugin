package io.lama06.zombies.system.weapon.reload;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import io.lama06.zombies.event.weapon.WeaponReloadChangeEvent;
import io.lama06.zombies.weapon.AmmoData;
import io.lama06.zombies.weapon.ReloadData;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class StartReloadAutoSystem implements Listener {
    @EventHandler
    private void onWeaponClipChange(final WeaponClipChangeEvent event) {
        if (event.getNewClip() != 0) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component ammoComponent = weapon.getComponent(Weapon.AMMO);
        if (ammoComponent == null) {
            return;
        }
        final int ammo = ammoComponent.get(AmmoData.AMMO);
        if (ammo == 0) {
            return;
        }
        final Component reloadComponent = weapon.getComponent(Weapon.RELOAD);
        if (reloadComponent == null) {
            return;
        }
        final int reload = weapon.getData().reload.reload();
        final int remainingReload = reloadComponent.get(ReloadData.REMAINING_RELOAD);
        reloadComponent.set(ReloadData.REMAINING_RELOAD, reload);
        Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(event.getWeapon(), remainingReload, reload));
    }
}
