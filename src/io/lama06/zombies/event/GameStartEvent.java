package io.lama06.zombies.event;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.util.HandlerListGetter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class GameStartEvent extends ZombiesEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GameStartEvent(final ZombiesWorld world) {
        super(world);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
