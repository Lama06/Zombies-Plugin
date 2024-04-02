package io.lama06.zombies.system.weapon.delay;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.DelayData;
import io.lama06.zombies.event.weapon.WeaponDelayChangeEvent;
import io.lama06.zombies.event.weapon.WeaponUseEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class StartDelayAfterWeaponUseSystem implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onWeaponUse(final WeaponUseEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.getComponent(WeaponComponents.DELAY);
        if (delayComponent == null) {
            return;
        }
        final int delay = delayComponent.get(DelayData.DELAY);
        final int remainingDelay = delayComponent.get(DelayData.REMAINING_DELAY);
        delayComponent.set(DelayData.REMAINING_DELAY, delay);
        Bukkit.getPluginManager().callEvent(new WeaponDelayChangeEvent(event.getWeapon(), remainingDelay, delay));
    }
}
