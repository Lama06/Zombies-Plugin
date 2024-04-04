package io.lama06.zombies.system.weapon.shoot;

import io.lama06.zombies.event.weapon.WeaponShootEvent;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.ShootParticleData;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public final class SpawnParticlesAfterShotSystem implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onWeaponShoot(final WeaponShootEvent event) {
        final ZombiesPlayer player = event.getPlayer();
        final Weapon weapon = event.getWeapon();
        final ShootParticleData shootParticleData = weapon.getData().shootParticle;
        if (shootParticleData == null) {
            return;
        }
        for (final WeaponShootEvent.Bullet bullet : event.getBullets()) {
            final Vector direction = bullet.direction().clone();
            final Vector directionWithDistance = direction.multiply(shootParticleData.spacing());
            final Location particleLocation = player.getBukkit().getEyeLocation().add(directionWithDistance);
            if (particleLocation.getBlock().getType() != Material.AIR) {
                break;
            }
            for (int i = 0; i < shootParticleData.count(); i++) {
                player.getWorld().getBukkit().spawnParticle(shootParticleData.particle(), particleLocation, 1, 0, 0, 0, 0);
                particleLocation.add(directionWithDistance);
            }
        }
    }
}
