package io.lama06.zombies.system.zombie.explosion_attack;

import io.lama06.zombies.zombie.ExplosionAttackData;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public final class ExplodeOnDeathSystem implements Listener {
    @EventHandler
    private void onEntityDeath(final EntityDeathEvent event) {
        final Zombie zombie = new Zombie(event.getEntity());
        if (!zombie.isZombie()) {
            return;
        }
        final ExplosionAttackData explosionAttackData = zombie.getData().explosionAttack;
        if (explosionAttackData == null) {
            return;
        }
        final boolean explodeOnDeath = explosionAttackData.onDeath();
        if (!explodeOnDeath) {
            return;
        }
        ZombieExplosion.explode(zombie, explosionAttackData);
    }
}
