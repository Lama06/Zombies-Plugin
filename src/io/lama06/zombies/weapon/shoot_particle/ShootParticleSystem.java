package io.lama06.zombies.weapon.shoot_particle;

import io.lama06.zombies.util.pdc.RegistryPersistentDataType;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponAttributes;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.shoot.WeaponShootEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public final class ShootParticleSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final ShootParticleData data = event.getData().shootParticle();
        if (data == null) {
            return;
        }
        final PersistentDataContainer pdc = event.getPdc();
        final PersistentDataContainer particleData = pdc.getAdapterContext().newPersistentDataContainer();
        final RegistryPersistentDataType<Particle> particleType = new RegistryPersistentDataType<>(Particle.class, Registry.PARTICLE_TYPE);
        particleData.set(ShootParticleAttributes.PARTICLE.getKey(), particleType, data.particle());
        particleData.set(ShootParticleAttributes.COUNT.getKey(), PersistentDataType.INTEGER, data.numberOfParticles());
        particleData.set(ShootParticleAttributes.SPACING.getKey(), PersistentDataType.DOUBLE, data.spacing());
        pdc.set(WeaponAttributes.SHOOT_PARTICLE.getKey(), PersistentDataType.TAG_CONTAINER, particleData);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onShoot(final WeaponShootEvent event) {
        final Player player = event.getPlayer();
        final Weapon weapon = event.getWeapon();
        final ItemStack item = weapon.getItem();
        if (item == null) {
            return;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer particleContainer = pdc.get(WeaponAttributes.SHOOT_PARTICLE.getKey(), PersistentDataType.TAG_CONTAINER);
        if (particleContainer == null) {
            return;
        }
        final RegistryPersistentDataType<Particle> particleType = new RegistryPersistentDataType<>(Particle.class, Registry.PARTICLE_TYPE);
        final Particle particle = particleContainer.get(ShootParticleAttributes.PARTICLE.getKey(), particleType);
        final Integer particleCount = particleContainer.get(ShootParticleAttributes.COUNT.getKey(), PersistentDataType.INTEGER);
        final Double particleSpacing = particleContainer.get(ShootParticleAttributes.SPACING.getKey(), PersistentDataType.DOUBLE);
        if (particle == null || particleCount == null || particleSpacing == null) {
            return;
        }
        for (final WeaponShootEvent.Bullet bullet : event.getBullets()) {
            final Vector direction = bullet.direction().clone();
            final Vector directionWithDistance = direction.multiply(particleSpacing);
            final Location particleLocation = player.getEyeLocation().add(directionWithDistance);
            if (particleLocation.getBlock().getType() != Material.AIR) {
                break;
            }
            for (int i = 0; i < particleCount; i++) {
                player.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
                particleLocation.add(directionWithDistance);
            }
        }
    }
}
