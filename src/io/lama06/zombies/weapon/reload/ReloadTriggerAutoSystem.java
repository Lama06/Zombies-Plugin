package io.lama06.zombies.weapon.reload;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.ammo.WeaponAmmoChangeEvent;
import org.bukkit.event.EventHandler;

public final class ReloadTriggerAutoSystem extends System {
    public ReloadTriggerAutoSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onClipGoesToZero(final WeaponAmmoChangeEvent event) {
        if (event.getNewClip() != 0) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        if (weapon.getReload() == null) {
            return;
        }
        weapon.getReload().startReload();
    }
}
