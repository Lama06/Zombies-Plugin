package io.lama06.zombies.event;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.util.HandlerListGetter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class StartRoundEvent extends ZombiesEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final int round;

    public StartRoundEvent(final ZombiesWorld world, final int round) {
        super(world);
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
