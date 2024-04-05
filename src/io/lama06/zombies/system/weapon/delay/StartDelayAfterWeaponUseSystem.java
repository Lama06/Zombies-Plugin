package io.lama06.zombies.system.weapon.delay;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.perk.PlayerPerk;
import io.lama06.zombies.weapon.Weapon;
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
        final Component delayComponent = weapon.getComponent(Weapon.DELAY);
        if (delayComponent == null) {
            return;
        }
        final int delay = weapon.getData().delay.delay();
        final int remainingDelay = delayComponent.get(DelayData.REMAINING_DELAY);
        final double delayFactor = event.getPlayer().hasPerk(PlayerPerk.QUICK_FIRE) ? 0.75 : 1;
        final int newDelay = (int) (delay * delayFactor);
        delayComponent.set(DelayData.REMAINING_DELAY, newDelay);
        Bukkit.getPluginManager().callEvent(new WeaponDelayChangeEvent(event.getWeapon(), remainingDelay, newDelay));
    }
}
