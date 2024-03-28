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

    private final int oldHealth;
    private final int newHealth;

    public ZombieHealthChangeEvent(final Zombie zombie, final int oldHealth, final int newHealth) {
        super(zombie);
        this.oldHealth = oldHealth;
        this.newHealth = newHealth;
    }

    public int getOldHealth() {
        return oldHealth;
    }

    public int getNewHealth() {
        return newHealth;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
