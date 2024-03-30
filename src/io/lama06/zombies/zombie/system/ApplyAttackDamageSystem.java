package io.lama06.zombies.zombie.system;

import io.lama06.zombies.event.PlayerAttacksZombieEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class ApplyAttackDamageSystem implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onAttack(final PlayerAttacksZombieEvent event) {
        final Entity zombie = event.getZombie();
        if (!(zombie instanceof final LivingEntity living)) {
            return;
        }
        living.damage(event.getDamage(), event.getPlayer());
        if (event.getFire()) {
            zombie.setFireTicks(5 * 20);
        }
    }
}
