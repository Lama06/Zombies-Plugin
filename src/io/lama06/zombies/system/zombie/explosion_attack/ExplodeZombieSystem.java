package io.lama06.zombies.system.zombie.explosion_attack;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.ExplosionAttackData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public final class ExplodeZombieSystem implements Listener {
    private static final int PARTICLE_COUNT = 4;
    private static final double DAMAGE_REACH = 5;

    private void explode(final Zombie zombie, final Component explosionAttackComponent) {
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

    @EventHandler
    private void onEntityDeath(final EntityDeathEvent event) {
        final Zombie zombie = new Zombie(event.getEntity());
        final Component explosionAttackComponent = zombie.getComponent(ZombieComponents.EXPLOSION_ATTACK);
        if (explosionAttackComponent == null) {
            return;
        }
        final boolean explodeOnDeath = explosionAttackComponent.get(ExplosionAttackData.ON_DEATH);
        if (!explodeOnDeath) {
            return;
        }
        explode(zombie, explosionAttackComponent);
    }

    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        final List<Zombie> zombies = ZombiesPlugin.INSTANCE.getZombies();
        for (final Zombie zombie : zombies) {
            final Component explosionAttackComponent = zombie.getComponent(ZombieComponents.EXPLOSION_ATTACK);
            if (explosionAttackComponent == null) {
                continue;
            }
            final int delay = explosionAttackComponent.getOrDefault(ExplosionAttackData.DELAY, 0);
            if (delay == 0) {
                continue;
            }
            if (event.getTickNumber() % delay != 0) {
                continue;
            }
            explode(zombie, explosionAttackComponent);
        }
    }
}
