package io.lama06.zombies.weapon.shoot_particle;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.shoot.WeaponShootEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

public final class ShootParticleSystem extends System {
    public ShootParticleSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler(ignoreCancelled = true)
    private void onShoot(final WeaponShootEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        final ShootParticleComponent shootParticle = event.getWeapon().getShootParticle();
        if (shootParticle == null) {
            return;
        }
        final ZombiesPlayer player = event.getWeapon().getPlayer();
        for (final WeaponShootEvent.Bullet bullet : event.getBullets()) {
            final Vector direction = bullet.direction().clone();
            final Vector directionWithDistance = direction.multiply(shootParticle.getDistanceBetweenParticles());
            final Location particleLocation = player.getBukkit().getLocation().add(directionWithDistance);
            if (particleLocation.getBlock().getType() != Material.AIR) {
                break;
            }
            for (int i = 0; i < shootParticle.getNumberOfParticles(); i++) {
                game.getWorld().spawnParticle(shootParticle.getParticle(), particleLocation, 1, 0, 0, 0, 0);
                particleLocation.add(directionWithDistance);
            }
        }
    }
}
