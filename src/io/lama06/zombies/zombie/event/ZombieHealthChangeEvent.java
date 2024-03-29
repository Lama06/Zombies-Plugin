package io.lama06.zombies.zombie.event;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class ZombieHealthChangeEvent extends ZombieEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final double oldHealth;
    private final double newHealth;

    public ZombieHealthChangeEvent(final Zombie zombie, final double oldHealth, final double newHealth) {
        super(zombie);
        this.oldHealth = oldHealth;
        this.newHealth = newHealth;
    }

    public double getOldHealth() {
        return oldHealth;
    }

    public double getNewHealth() {
        return newHealth;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
