package io.lama06.zombies.system.weapon.delay;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.DelayData;
import io.lama06.zombies.event.weapon.WeaponDelayChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class TickDelaySystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        for (final Weapon weapon : ZombiesPlugin.INSTANCE.getWeapons()) {
            final Component delayComponent = weapon.getComponent(WeaponComponents.DELAY);
            if (delayComponent == null) {
                continue;
            }
            final int remainingDelay = delayComponent.get(DelayData.REMAINING_DELAY);
            if (remainingDelay == 0) {
                continue;
            }
            delayComponent.set(DelayData.REMAINING_DELAY, remainingDelay - 1);
            Bukkit.getPluginManager().callEvent(new WeaponDelayChangeEvent(weapon, remainingDelay, remainingDelay - 1));
        }
    }
}
