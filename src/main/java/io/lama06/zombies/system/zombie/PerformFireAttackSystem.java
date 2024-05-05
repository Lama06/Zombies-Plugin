package io.lama06.zombies.system.zombie;

import io.lama06.zombies.zombie.FireAttackData;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class PerformFireAttackSystem implements Listener {
    @EventHandler
    private void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Zombie damager = new Zombie(event.getDamager());
        if (!damager.isZombie()) {
            return;
        }
        final FireAttackData fireAttackData = damager.getData().fireAttack;
        if (fireAttackData == null) {
            return;
        }
        final Entity entity = event.getEntity();
        entity.setFireTicks(fireAttackData.ticks());
    }
}
