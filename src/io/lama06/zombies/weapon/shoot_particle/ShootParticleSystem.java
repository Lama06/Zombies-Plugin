package io.lama06.zombies.weapon.shoot_particle;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.shoot.Bullet;
import io.lama06.zombies.weapon.shoot.WeaponShootEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public final class ShootParticleSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final ShootParticleData data = event.getData().shootParticle();
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component shootParticleComponent = weapon.addComponent(WeaponComponents.SHOOT_PARTICLE);
        shootParticleComponent.set(ShootParticleAttributes.PARTICLE, data.particle());
        shootParticleComponent.set(ShootParticleAttributes.COUNT, data.count());
        shootParticleComponent.set(ShootParticleAttributes.SPACING, data.spacing());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void createParticlesAfterShot(final WeaponShootEvent event) {
        final ZombiesPlayer player = event.getPlayer();
        final Weapon weapon = event.getWeapon();
        final Component shootParticleComponent = weapon.getComponent(WeaponComponents.SHOOT_PARTICLE);
        if (shootParticleComponent == null) {
            return;
        }
        final Particle particle = shootParticleComponent.get(ShootParticleAttributes.PARTICLE);
        final int count = shootParticleComponent.get(ShootParticleAttributes.COUNT);
        final double spacing = shootParticleComponent.get(ShootParticleAttributes.SPACING);
        for (final Bullet bullet : event.getBullets()) {
            final Vector direction = bullet.direction().clone();
            final Vector directionWithDistance = direction.multiply(spacing);
            final Location particleLocation = player.getBukkit().getEyeLocation().add(directionWithDistance);
            if (particleLocation.getBlock().getType() != Material.AIR) {
                break;
            }
            for (int i = 0; i < count; i++) {
                player.getWorld().getBukkit().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
                particleLocation.add(directionWithDistance);
            }
        }
    }
}
