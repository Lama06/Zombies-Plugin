package io.lama06.zombies.event;

import org.bukkit.World;
import org.bukkit.event.Event;

public abstract class ZombiesEvent extends Event {
    private final World world;

    protected ZombiesEvent(final World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
