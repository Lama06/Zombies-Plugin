package io.lama06.zombies.system.weapon.ammo;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.AmmoData;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitAmmoSystem implements Listener {
    @EventHandler
    private void onWeaponCreate(final WeaponCreateEvent event) {
        final AmmoData data = event.getData().ammo;
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        weapon.getItem().setAmount(data.clip());
        final Component ammoComponent = weapon.addComponent(WeaponComponents.AMMO);
        ammoComponent.set(AmmoData.MAX_AMMO, data.ammo());
        ammoComponent.set(AmmoData.AMMO, data.ammo());
        ammoComponent.set(AmmoData.MAX_CLIP, data.clip());
        ammoComponent.set(AmmoData.CLIP, data.clip());
    }
}
