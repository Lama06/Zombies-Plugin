package io.lama06.zombies.event;

import io.lama06.zombies.ZombiesWorld;
import org.bukkit.event.Event;

public abstract class ZombiesEvent extends Event {
    private final ZombiesWorld world;

    protected ZombiesEvent(final ZombiesWorld world) {
        this.world = world;
    }

    public ZombiesWorld getWorld() {
        return world;
    }
}
