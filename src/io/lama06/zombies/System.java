package io.lama06.zombies;

import org.bukkit.World;
import org.bukkit.event.Listener;

public abstract class System implements Listener {
    protected final ZombiesGame game;
    protected final World world;

    protected System(final ZombiesGame game) {
        this.game = game;
        world = game.getWorld();
    }
}
