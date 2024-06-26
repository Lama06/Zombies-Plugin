package io.lama06.zombies.system.weapon.delay;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.DelayData;
import io.lama06.zombies.event.weapon.WeaponUseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PreventWeaponUseDuringDelaySystem implements Listener {
    @EventHandler(ignoreCancelled = true)
    private void preventWeaponUseDuringDelay(final WeaponUseEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.getComponent(Weapon.DELAY);
        if (delayComponent == null) {
            return;
        }
        final int remainingDelay = delayComponent.get(DelayData.REMAINING_DELAY);
        if (remainingDelay > 0) {
            event.setCancelled(true);
        }
    }
}
