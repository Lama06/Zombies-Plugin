package io.lama06.zombies.system.weapon.melee;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.MeleeAttributes;
import io.lama06.zombies.weapon.MeleeData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitMeleeSystem implements Listener {
    @EventHandler
    private void onWeaponCreate(final WeaponCreateEvent event) {
        final MeleeData data = event.getData().melee();
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component meleeComponent = weapon.addComponent(WeaponComponents.MELEE);
        meleeComponent.set(MeleeAttributes.RANGE, data.range());
    }
}
