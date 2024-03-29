package io.lama06.zombies.zombie.component;

import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.event.ZombieHealthChangeEvent;
import org.bukkit.Bukkit;

public final class HealthComponent extends ZombieComponent {
    private final double maxHealth;
    private double health;

    public HealthComponent(final Zombie zombie, final int maxHealth) {
        super(zombie);
        this.maxHealth = maxHealth;
        health = maxHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(final double health) {
        final double oldHealth = this.health;
        this.health = health;
        Bukkit.getPluginManager().callEvent(new ZombieHealthChangeEvent(zombie, oldHealth, health));
    }

    public void damage(final double damage) {
        setHealth(Math.max(health - damage, 0));
    }
}
