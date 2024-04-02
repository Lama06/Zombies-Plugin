package io.lama06.zombies.system.zombie;

import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitZombieHealthSystem implements Listener {
    @EventHandler
    private void onSpawn(final ZombieSpawnEvent event) {
        final int health = event.getData().health;
        if (health == 0) {
            return;
        }
        final Entity entity = event.getZombie().getEntity();
        if (!(entity instanceof final LivingEntity living)) {
            return;
        }
        final AttributeInstance maxHealth = living.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }
        maxHealth.setBaseValue(health);
        living.setHealth(health);
    }
}
