package io.lama06.zombies.event;

import io.lama06.zombies.util.EventHandlerAccess;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class GameStartEvent extends ZombiesEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GameStartEvent(final World world) {
        super(world);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
