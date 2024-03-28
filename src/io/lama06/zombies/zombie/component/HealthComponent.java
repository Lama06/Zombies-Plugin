package io.lama06.zombies.zombie.component;

import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.event.ZombieHealthChangeEvent;
import org.bukkit.Bukkit;

public final class HealthComponent extends ZombieComponent {
    private final int maxHealth;
    private int health;

    public HealthComponent(final Zombie zombie, final int maxHealth) {
        super(zombie);
        this.maxHealth = maxHealth;
        health = maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        final int oldHealth = this.health;
        this.health = health;
        Bukkit.getPluginManager().callEvent(new ZombieHealthChangeEvent(zombie, oldHealth, health));
    }

    public void damage(final int damage) {
        setHealth(Math.min(health - damage, 0));
    }
}
