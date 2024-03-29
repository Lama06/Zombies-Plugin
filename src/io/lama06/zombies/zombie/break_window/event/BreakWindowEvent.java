package io.lama06.zombies.zombie.break_window.event;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.event.ZombieEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class BreakWindowEvent extends ZombieEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public BreakWindowEvent(final Zombie zombie) {
        super(zombie);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
