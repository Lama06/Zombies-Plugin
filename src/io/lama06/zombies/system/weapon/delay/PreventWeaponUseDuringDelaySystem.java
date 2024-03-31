package io.lama06.zombies.system.weapon.delay;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.DelayAttributes;
import io.lama06.zombies.event.weapon.WeaponUseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PreventWeaponUseDuringDelaySystem implements Listener {
    @EventHandler(ignoreCancelled = true)
    private void preventWeaponUseDuringDelay(final WeaponUseEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.getComponent(WeaponComponents.DELAY);
        if (delayComponent == null) {
            return;
        }
        final int remainingDelay = delayComponent.get(DelayAttributes.REMAINING_DELAY);
        if (remainingDelay > 0) {
            event.setCancelled(true);
        }
    }
}
