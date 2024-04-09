package io.lama06.zombies.event;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.util.HandlerListGetter;
import org.bukkit.event.HandlerList;

public final class GameEndEvent extends ZombiesEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GameEndEvent(final ZombiesWorld world) {
        super(world);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
