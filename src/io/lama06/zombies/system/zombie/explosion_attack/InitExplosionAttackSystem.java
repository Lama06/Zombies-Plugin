package io.lama06.zombies.system.zombie.explosion_attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.zombie.ExplosionAttackData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitExplosionAttackSystem implements Listener {
    @EventHandler
    private void onZombieSpawn(final ZombieSpawnEvent event) {
        final Zombie zombie = event.getZombie();
        final ExplosionAttackData explosionAttack = event.getData().explosionAttack;
        if (explosionAttack == null) {
            return;
        }
        final Component explosionAttackComponent = zombie.addComponent(ZombieComponents.EXPLOSION_ATTACK);
        explosionAttackComponent.set(ExplosionAttackData.ON_DEATH, explosionAttack.onDeath());
        explosionAttackComponent.set(ExplosionAttackData.DAMAGE, explosionAttack.damage());
        if (explosionAttack.delay() != null) {
            explosionAttackComponent.set(ExplosionAttackData.DELAY, explosionAttack.delay());
        }
    }
}
