package io.lama06.zombies.system.weapon.reload;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.event.weapon.WeaponUseEvent;
import io.lama06.zombies.weapon.ReloadData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PreventWeaponUseDuringReloadSystem implements Listener {
    @EventHandler(ignoreCancelled = true)
    private void onWeaponUse(final WeaponUseEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        if (reloadComponent == null) {
            return;
        }
        final int remainingReload = reloadComponent.get(ReloadData.REMAINING_RELOAD);
        if (remainingReload > 0) {
            event.setCancelled(true);
        }
    }
}
