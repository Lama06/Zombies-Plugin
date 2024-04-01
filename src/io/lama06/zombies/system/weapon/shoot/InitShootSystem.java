package io.lama06.zombies.system.weapon.shoot;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import io.lama06.zombies.weapon.ShootAttributes;
import io.lama06.zombies.weapon.ShootData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitShootSystem implements Listener {
    @EventHandler
    private void onWeaponCreate(final WeaponCreateEvent event) {
        final ShootData data = event.getData().shoot;
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component shootComponent = weapon.addComponent(WeaponComponents.SHOOT);
        shootComponent.set(ShootAttributes.BULLETS, data.bullets());
        shootComponent.set(ShootAttributes.PRECISION, data.precision());
    }
}
