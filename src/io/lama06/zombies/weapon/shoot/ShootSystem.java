package io.lama06.zombies.weapon.shoot;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.VectorUtil;
import io.lama06.zombies.weapon.Weapon;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public final class ShootSystem extends System {
    public ShootSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onPlayerAttackEntity(final PrePlayerAttackEntityEvent event) {
        if (!game.getPlayers().containsKey(event.getPlayer())) {
            return;
        }
        final ZombiesPlayer player = game.getPlayers().get(event.getPlayer());
        onLeftClick(player);
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!game.getPlayers().containsKey(event.getPlayer())) {
            return;
        }
        final ZombiesPlayer player = game.getPlayers().get(event.getPlayer());
        if (!event.getAction().isLeftClick()) {
            return;
        }
        onLeftClick(player);
    }

    private void onLeftClick(final ZombiesPlayer player) {
        final Weapon heldWeapon = player.getHeldWeapon();
        if (heldWeapon == null || heldWeapon.getShoot() == null) {
            return;
        }
        final ShootComponent shoot = heldWeapon.getShoot();

        final RandomGenerator rnd = ThreadLocalRandom.current();
        final List<WeaponShootEvent.Bullet> bullets = new ArrayList<>(shoot.getBullets());
        for (int i = 0; i < shoot.getBullets(); i++) {
            final float yaw = (float) (player.getBukkit().getYaw() +
                    (1 - shoot.getPrecision()) * rnd.nextDouble() * 10 * (rnd.nextBoolean() ? 1 : -1));
            final float pitch = (float) (player.getBukkit().getPitch() +
                    (1 - shoot.getPrecision()) * rnd.nextDouble() * 5 * (rnd.nextBoolean() ? 1 : -1));
            final Vector bulletDirection = VectorUtil.fromJawAndPitch(yaw, pitch);
            bullets.add(new WeaponShootEvent.Bullet(bulletDirection));
        }
        Bukkit.getPluginManager().callEvent(new WeaponShootEvent(heldWeapon, bullets));
    }
}
