package io.lama06.zombies.event.zombie;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieData;
import org.bukkit.event.HandlerList;

public final class ZombieSpawnEvent extends ZombieEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final ZombieData data;

    public ZombieSpawnEvent(final Zombie zombie, final ZombieData data) {
        super(zombie);
        this.data = data;
    }

    public ZombieData getData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
