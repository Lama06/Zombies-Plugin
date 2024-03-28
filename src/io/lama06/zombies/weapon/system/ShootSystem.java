package io.lama06.zombies.weapon.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.VectorUtil;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.component.AmmoComponent;
import io.lama06.zombies.weapon.component.ShootComponent;
import io.lama06.zombies.weapon.event.WeaponShootEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class ShootSystem extends System {
    public ShootSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!game.getPlayers().containsKey(event.getPlayer())) {
            return;
        }
        final ZombiesPlayer player = game.getPlayers().get(event.getPlayer());
        event.setCancelled(true);

        if (!event.getAction().isLeftClick()) {
            return;
        }

        final Weapon heldWeapon = player.getHeldWeapon();
        if (heldWeapon == null || heldWeapon.getShoot() == null) {
            return;
        }
        final ShootComponent shoot = heldWeapon.getShoot();

        final AmmoComponent ammo = heldWeapon.getAmmo();
        if (ammo == null) {
            return;
        }
        if (ammo.getClip() == 0) {
            return;
        }
        ammo.setClip(ammo.getClip() - 1);

        if (heldWeapon.getDelay() != null && !heldWeapon.getDelay().isReady()) {
            return;
        }
        if (heldWeapon.getReload() != null && !heldWeapon.getReload().isReady()) {
            return;
        }

        final List<WeaponShootEvent.Bullet> bullets = new ArrayList<>(shoot.getBullets());
        final Vector playerDirection = VectorUtil.fromPlayerLookingDirection(event.getPlayer());
        for (int i = 0; i < shoot.getBullets(); i++) {
            final Vector bulletDirection = playerDirection.clone();
            //VectorUtil.applyInaccuracy(bulletDirection,  (1 - shoot.getPrecision()));
            bullets.add(new WeaponShootEvent.Bullet(bulletDirection));
        }
        if (heldWeapon.getDelay() != null) {
            heldWeapon.getDelay().startDelay();
        }
        Bukkit.getPluginManager().callEvent(new WeaponShootEvent(heldWeapon, playerDirection, bullets));
    }
}
