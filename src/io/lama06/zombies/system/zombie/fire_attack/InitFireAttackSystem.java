package io.lama06.zombies.system.zombie.fire_attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.zombie.FireAttackData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitFireAttackSystem implements Listener {
    @EventHandler
    private void onZombieSpawn(final ZombieSpawnEvent event) {
        final Zombie zombie = event.getZombie();
        final FireAttackData fireAttack = event.getData().fireAttack;
        if (fireAttack == null) {
            return;
        }
        final Component fireAttackComponent = zombie.addComponent(ZombieComponents.FIRE_ATTACK);
        fireAttackComponent.set(FireAttackData.TICKS, fireAttack.ticks());
    }
}
