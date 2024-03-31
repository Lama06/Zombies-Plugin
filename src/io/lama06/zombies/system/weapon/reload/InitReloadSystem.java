package io.lama06.zombies.system.weapon.reload;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import io.lama06.zombies.weapon.ReloadAttributes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitReloadSystem implements Listener {
    @EventHandler
    private void onWeaponCreate(final WeaponCreateEvent event) {
        final Integer reload = event.getData().reload();
        if (reload == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component reloadComponent = weapon.addComponent(WeaponComponents.RELOAD);
        reloadComponent.set(ReloadAttributes.RELOAD, reload);
        reloadComponent.set(ReloadAttributes.REMAINING_RELOAD, 0);
    }
}
