package io.lama06.zombies.system.weapon.ammo;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.AmmoData;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.event.weapon.WeaponAmmoChangeEvent;
import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import io.lama06.zombies.event.weapon.WeaponReloadChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ReloadAmmoSystem implements Listener {
    @EventHandler
    private void onWeaponReloadChange(final WeaponReloadChangeEvent event) {
        if (!event.isComplete()) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component ammoComponent = weapon.getComponent(WeaponComponents.AMMO);
        if (ammoComponent == null) {
            return;
        }
        final int ammo = ammoComponent.get(AmmoData.AMMO);
        final int maxClip = ammoComponent.get(AmmoData.MAX_CLIP);
        final int clip = ammoComponent.get(AmmoData.CLIP);
        final int missingFromClip = maxClip - clip;
        final int addToClip = Math.min(missingFromClip, maxClip);
        final int newClip = clip + addToClip;
        final int newAmmo = ammo - addToClip;
        ammoComponent.set(AmmoData.CLIP, newClip);
        ammoComponent.set(AmmoData.AMMO, newAmmo);
        Bukkit.getPluginManager().callEvent(new WeaponClipChangeEvent(weapon, clip, newClip));
        Bukkit.getPluginManager().callEvent(new WeaponAmmoChangeEvent(weapon, ammo, newAmmo));
    }
}
