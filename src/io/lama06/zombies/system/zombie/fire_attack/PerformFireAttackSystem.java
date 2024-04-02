package io.lama06.zombies.system.zombie.fire_attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.FireAttackData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class PerformFireAttackSystem implements Listener {
    @EventHandler
    private void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Zombie damager = new Zombie(event.getDamager());
        final Component fireAttackComponent = damager.getComponent(ZombieComponents.FIRE_ATTACK);
        if (fireAttackComponent == null) {
            return;
        }
        final int fireTicks = fireAttackComponent.get(FireAttackData.TICKS);
        final Entity entity = event.getEntity();
        entity.setFireTicks(fireTicks);
    }
}
