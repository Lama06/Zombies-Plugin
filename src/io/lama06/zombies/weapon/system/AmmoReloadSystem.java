package io.lama06.zombies.weapon.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponReloadChangeEvent;
import org.bukkit.event.EventHandler;

public final class AmmoReloadSystem extends System {
    public AmmoReloadSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onReload(final WeaponReloadChangeEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        if (!event.isComplete()) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        if (weapon.getAmmo() == null) {
            return;
        }
        final int missingFromClip = weapon.getAmmo().getMaxClip() - weapon.getAmmo().getClip();
        final int addToClip = Math.min(missingFromClip, weapon.getAmmo().getAmmo());
        weapon.getAmmo().setClip(weapon.getAmmo().getClip() + addToClip);
        weapon.getAmmo().setAmmo(weapon.getAmmo().getAmmo() - addToClip);
    }
}
