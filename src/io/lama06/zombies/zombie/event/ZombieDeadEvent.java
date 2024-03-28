package io.lama06.zombies.zombie.event;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class ZombieDeadEvent extends ZombieEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ZombieDeadEvent(final Zombie zombie) {
        super(zombie);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
