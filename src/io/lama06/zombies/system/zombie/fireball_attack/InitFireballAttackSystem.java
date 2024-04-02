package io.lama06.zombies.system.zombie.fireball_attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.zombie.FireBallAttackData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitFireballAttackSystem implements Listener {
    @EventHandler
    private void onZombieSpawn(final ZombieSpawnEvent event) {
        final FireBallAttackData fireBallAttack = event.getData().fireBallAttack;
        if (fireBallAttack == null) {
            return;
        }
        final Zombie zombie = event.getZombie();
        final Component fireBallAttackComponent = zombie.addComponent(ZombieComponents.FIRE_BALL_ATTACK);
        fireBallAttackComponent.set(FireBallAttackData.DAMAGE, fireBallAttack.damage());
        fireBallAttackComponent.set(FireBallAttackData.DELAY, fireBallAttack.delay());
    }
}
