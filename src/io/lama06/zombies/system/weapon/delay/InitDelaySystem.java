package io.lama06.zombies.system.weapon.delay;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.DelayAttributes;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitDelaySystem implements Listener {
    @EventHandler
    private void onWeaponCreate(final WeaponCreateEvent event) {
        final Integer delay = event.getData().delay();
        if (delay == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.addComponent(WeaponComponents.DELAY);
        delayComponent.set(DelayAttributes.DELAY, delay);
        delayComponent.set(DelayAttributes.REMAINING_DELAY, 0);
    }
}
