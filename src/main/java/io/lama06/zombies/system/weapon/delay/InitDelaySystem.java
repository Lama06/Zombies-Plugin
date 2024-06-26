package io.lama06.zombies.system.weapon.delay;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.DelayData;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitDelaySystem implements Listener {
    @EventHandler
    private void onWeaponCreate(final WeaponCreateEvent event) {
        final DelayData delay = event.getData().delay;
        if (delay == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.addComponent(Weapon.DELAY);
        delayComponent.set(DelayData.REMAINING_DELAY, 0);
    }
}
