package io.lama06.zombies.weapon.ammo;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.weapon.shoot.WeaponShootEvent;
import org.bukkit.event.EventHandler;

public final class DecrementClipOnShootSystem extends System {
    public DecrementClipOnShootSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler(ignoreCancelled = true)
    private void onShoot(final WeaponShootEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        if (event.getWeapon().getAmmo() == null) {
            return;
        }
        event.getWeapon().getAmmo().setClip(event.getWeapon().getAmmo().getClip() - 1);
    }
}
