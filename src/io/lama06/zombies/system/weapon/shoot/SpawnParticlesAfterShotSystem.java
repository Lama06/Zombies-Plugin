package io.lama06.zombies.system.weapon.shoot;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.ShootParticleData;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.event.weapon.WeaponShootEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public final class SpawnParticlesAfterShotSystem implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onWeaponShoot(final WeaponShootEvent event) {
        final ZombiesPlayer player = event.getPlayer();
        final Weapon weapon = event.getWeapon();
        final Component shootParticleComponent = weapon.getComponent(WeaponComponents.SHOOT_PARTICLE);
        if (shootParticleComponent == null) {
            return;
        }
        final Particle particle = shootParticleComponent.get(ShootParticleData.PARTICLE);
        final int count = shootParticleComponent.get(ShootParticleData.COUNT);
        final double spacing = shootParticleComponent.get(ShootParticleData.SPACING);
        for (final WeaponShootEvent.Bullet bullet : event.getBullets()) {
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
