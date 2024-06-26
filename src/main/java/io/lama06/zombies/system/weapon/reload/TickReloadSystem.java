package io.lama06.zombies.system.weapon.reload;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.ReloadData;
import io.lama06.zombies.event.weapon.WeaponReloadChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class TickReloadSystem implements Listener {
    @EventHandler
    private void tickReload(final ServerTickEndEvent event) {
        for (final Weapon weapon : ZombiesPlugin.INSTANCE.getWeapons()) {
            final Component reloadComponent = weapon.getComponent(Weapon.RELOAD);
            if (reloadComponent == null) {
                continue;
            }
            final int remainingReload = reloadComponent.get(ReloadData.REMAINING_RELOAD);
            if (remainingReload == 0) {
                continue;
            }
            reloadComponent.set(ReloadData.REMAINING_RELOAD, remainingReload - 1);
            Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(weapon, remainingReload, remainingReload - 1));
        }
    }
}
