package io.lama06.zombies.system.zombie.explosion_attack;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.ExplosionAttackData;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

final class ZombieExplosion {
    private static final int PARTICLE_COUNT = 4;
    private static final double DAMAGE_REACH = 5;

    static void explode(final Zombie zombie, final Component explosionAttackComponent) {
        final double damage = explosionAttackComponent.get(ExplosionAttackData.DAMAGE);
        final ZombiesWorld world = zombie.getWorld();
        final World worldBukkit = world.getBukkit();
        worldBukkit.spawnParticle(Particle.EXPLOSION_LARGE, zombie.getEntity().getLocation(), PARTICLE_COUNT);
        worldBukkit.playSound(
                zombie.getEntity().getLocation(),
                Sound.ENTITY_GENERIC_EXPLODE,
                SoundCategory.HOSTILE,
                1,
                1
        );
        final List<Entity> nearbyEntities = zombie.getEntity().getNearbyEntities(DAMAGE_REACH, DAMAGE_REACH, DAMAGE_REACH);
        for (final Entity nearbyEntity : nearbyEntities) {
            if (!(nearbyEntity instanceof final Player player)) {
                continue;
            }
            player.damage(damage, zombie.getEntity());
        }
    }
}
