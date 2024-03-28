package io.lama06.zombies.event;

import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.util.EventHandlerAccess;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class GameStartEvent extends ZombiesEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    public GameStartEvent(final ZombiesGame game) {
        super(game);
    }

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
