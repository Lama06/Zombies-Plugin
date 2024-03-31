package io.lama06.zombies.weapon.shoot;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.PlayerAttacksZombieEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.util.VectorUtil;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
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

public final class ShootSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final ShootData data = event.getData().shoot();
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component shootComponent = weapon.addComponent(WeaponComponents.SHOOT);
        shootComponent.set(ShootAttributes.BULLETS, data.bullets());
        shootComponent.set(ShootAttributes.PRECISION, data.precision());
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component shootComponent = weapon.getComponent(WeaponComponents.SHOOT);
        if (shootComponent == null) {
            return;
        }
        final int bullets = shootComponent.get(ShootAttributes.BULLETS);
        final double precision = shootComponent.get(ShootAttributes.PRECISION);
        event.addLore(LorePart.SHOOT, List.of(
                new LoreEntry("Bullets", Integer.toString(bullets)),
                new LoreEntry("Precision", Math.round(precision * 100) + "%")
        ));
    }

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
        if (!ZombiesPlayer.isZombiesPlayer(bukkit)) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(bukkit);
        final Weapon weapon = player.getHeldWeapon();
        if (weapon == null) {
            return;
        }
        final Component shootComponent = weapon.getComponent(WeaponComponents.SHOOT);
        if (shootComponent == null) {
            return;
        }
        final int bullets = shootComponent.get(ShootAttributes.BULLETS);
        final double precision = shootComponent.get(ShootAttributes.PRECISION);
        final RandomGenerator rnd = ThreadLocalRandom.current();
        final List<Bullet> bulletsList = new ArrayList<>();
        for (int i = 0; i < bullets; i++) {
            final float yaw = (float) (player.getBukkit().getYaw() +
                    (1 - precision) * rnd.nextDouble() * 10 * (rnd.nextBoolean() ? 1 : -1));
            final float pitch = (float) (player.getBukkit().getPitch() +
                    (1 - precision) * rnd.nextDouble() * 5 * (rnd.nextBoolean() ? 1 : -1));
            final Vector bulletDirection = VectorUtil.fromJawAndPitch(yaw, pitch);
            bulletsList.add(new Bullet(bulletDirection));
        }
        if (!new WeaponShootEvent(weapon, bulletsList).callEvent()) {
            return;
        }
        for (final Bullet bullet : bulletsList) {
            detectShotAtZombie(player, weapon, bullet);
        }
    }

    private void detectShotAtZombie(final ZombiesPlayer player, final Weapon weapon, final Bullet bullet) {
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
        if (!Zombie.isZombie(entity)) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerAttacksZombieEvent(weapon, new Zombie(entity)));
    }
}
