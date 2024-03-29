package io.lama06.zombies.weapon.ammo;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.render.WeaponRenderItemEvent;
import io.lama06.zombies.weapon.render.WeaponRenderItemRequestEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public final class AmmoClipRenderSystem extends System {
    public AmmoClipRenderSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onAmmoChangeEvent(final WeaponAmmoChangeEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        if (event.getOldClip() == event.getNewClip()) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new WeaponRenderItemRequestEvent(event.getWeapon()));
    }

    @EventHandler
    private void onWeaponRender(final WeaponRenderItemEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        if (weapon.getAmmo() != null && weapon.getAmmo().getClip() != 0) {
            event.getItem().setAmount(weapon.getAmmo().getClip());
        }
    }
}
