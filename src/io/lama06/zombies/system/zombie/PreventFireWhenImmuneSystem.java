package io.lama06.zombies.system.zombie;

import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieAttributes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class PreventFireWhenImmuneSystem implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerAttackZombie(final PlayerAttackZombieEvent event) {
        final Zombie zombie = event.getZombie();
        final boolean fireImmune = zombie.getOrDefault(ZombieAttributes.FIRE_IMMUNE, false);
        if (!fireImmune) {
            return;
        }
        event.setFire(false);
    }
}
