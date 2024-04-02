package io.lama06.zombies.system.zombie.explosion_attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.ExplosionAttackData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public final class ExplodeOnDeathSystem implements Listener {
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
        ZombieExplosion.explode(zombie, explosionAttackComponent);
    }
}
