package io.lama06.zombies.system.weapon.ammo;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.AmmoData;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import io.lama06.zombies.event.weapon.WeaponUseEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class DecrementAmmoAfterWeaponUseSystem implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onWeaponUse(final WeaponUseEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component ammoComponent = weapon.getComponent(Weapon.AMMO);
        if (ammoComponent == null) {
            return;
        }
        final int clip = ammoComponent.get(AmmoData.CLIP);
        if (clip == 0) {
            return;
        }
        ammoComponent.set(AmmoData.CLIP, clip - 1);
        Bukkit.getPluginManager().callEvent(new WeaponClipChangeEvent(weapon, clip, clip - 1));
    }
}
