package io.lama06.zombies.system.weapon.reload;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import io.lama06.zombies.weapon.ReloadAttributes;
import io.lama06.zombies.event.weapon.WeaponReloadChangeEvent;
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
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        if (reloadComponent == null) {
            return;
        }
        final int reload = reloadComponent.get(ReloadAttributes.RELOAD);
        final int remainingReload = reloadComponent.get(ReloadAttributes.REMAINING_RELOAD);
        reloadComponent.set(ReloadAttributes.REMAINING_RELOAD, reload);
        Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(event.getWeapon(), remainingReload, reload));
    }

}