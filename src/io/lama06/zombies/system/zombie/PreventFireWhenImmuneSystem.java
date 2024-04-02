package io.lama06.zombies.system.zombie;

import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieAttributes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public final class PreventFireWhenImmuneSystem implements Listener {
    @EventHandler
    private void onZombieSpawn(final ZombieSpawnEvent event) {
        if (!event.getData().fireImmune) {
            return;
        }
        event.getZombie().set(ZombieAttributes.FIRE_IMMUNE, true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerAttackZombie(final PlayerAttackZombieEvent event) {
        final Zombie zombie = event.getZombie();
        final boolean fireImmune = zombie.getOrDefault(ZombieAttributes.FIRE_IMMUNE, false);
        if (!fireImmune) {
            return;
        }
        event.setFire(false);
    }

    @EventHandler
    private void onEntityDamage(final EntityDamageEvent event) {
        final Zombie zombie = new Zombie(event.getEntity());
        if (!zombie.isZombie()) {
            return;
        }
        final boolean fireImmune = zombie.getOrDefault(ZombieAttributes.FIRE_IMMUNE, false);
        if (!fireImmune) {
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.FIRE && event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) {
            return;
        }
        event.setCancelled(true);
    }
}
