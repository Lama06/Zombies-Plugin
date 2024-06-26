package io.lama06.zombies.system.weapon.shoot;

import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.event.weapon.WeaponShootEvent;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.VectorUtil;
import io.lama06.zombies.weapon.ShootData;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.zombie.Zombie;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public final class FireBulletsSystem implements Listener {
    @EventHandler
    private void onPlayerAttackEntity(final PrePlayerAttackEntityEvent event) {
        onLeftClick(event.getPlayer());
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
            return;
        }
        onLeftClick(event.getPlayer());
    }

    private void onLeftClick(final Player bukkit) {
        final ZombiesPlayer player = new ZombiesPlayer(bukkit);
        if (!player.getWorld().isGameRunning() || !player.isAlive()) {
            return;
        }
        final Weapon weapon = player.getHeldWeapon();
        if (weapon == null) {
            return;
        }
        final ShootData shootData = weapon.getData().shoot;
        if (shootData == null) {
            return;
        }
        final RandomGenerator rnd = ThreadLocalRandom.current();
        final List<WeaponShootEvent.Bullet> bulletsList = new ArrayList<>();
        for (int i = 0; i < shootData.bullets(); i++) {
            final float yaw = (float) (player.getBukkit().getYaw() +
                    (1 - shootData.precision()) * rnd.nextDouble() * 90 * (rnd.nextBoolean() ? 1 : -1));
            final float pitch = (float) (player.getBukkit().getPitch() +
                    (1 - shootData.precision()) * rnd.nextDouble() * 90 * (rnd.nextBoolean() ? 1 : -1));
            final Vector bulletDirection = VectorUtil.fromJawAndPitch(yaw, pitch);
            bulletsList.add(new WeaponShootEvent.Bullet(bulletDirection));
        }
        if (!new WeaponShootEvent(weapon, bulletsList).callEvent()) {
            return;
        }
        for (final WeaponShootEvent.Bullet bullet : bulletsList) {
            detectShotAtZombie(player, weapon, bullet);
        }
    }

    private void detectShotAtZombie(final ZombiesPlayer player, final Weapon weapon, final WeaponShootEvent.Bullet bullet) {
        final RayTraceResult ray = player.getWorld().getBukkit().rayTraceEntities(
                player.getBukkit().getEyeLocation(),
                bullet.direction(),
                50,
                entity -> !entity.equals(player.getBukkit())
        );
        if (ray == null) {
            return;
        }
        final Entity entity = ray.getHitEntity();
        final Zombie zombie = new Zombie(entity);
        if (!zombie.isZombie()) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerAttackZombieEvent(weapon, zombie));
    }
}
