package io.lama06.zombies.zombie.event;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.zombie.ZombieData;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class ZombieSpawnEvent extends ZombieEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final ZombieData data;

    public ZombieSpawnEvent(final Entity zombie, final ZombieData data) {
        super(zombie);
        this.data = data;
    }

    public ZombieData getData() {
        return data;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
