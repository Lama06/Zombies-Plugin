package io.lama06.zombies.system.zombie;

import io.lama06.zombies.event.PlayerAttackZombieEvent;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class DamageZombieAfterAttackSystem implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onAttack(final PlayerAttackZombieEvent event) {
        final Zombie zombie = event.getZombie();
        if (!(zombie.getEntity() instanceof final LivingEntity living)) {
            return;
        }
        living.damage(event.getDamage(), event.getPlayer().getBukkit());
        if (event.getFire()) {
            living.setFireTicks(5 * 20);
        }
    }
}
