package io.lama06.zombies.zombie.system;

import io.lama06.zombies.zombie.event.ZombieSpawnEvent;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitialHealthSystem implements Listener {
    @EventHandler
    private void onSpawn(final ZombieSpawnEvent event) {
        final int health = event.getData().health();
        final Entity zombie = event.getZombie();
        if (!(zombie instanceof final Attributable attributable)) {
            return;
        }
        final AttributeInstance maxHealth = attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }
        maxHealth.setBaseValue(health);
    }
}
