package io.lama06.zombies.event;

import io.lama06.zombies.ZombiesGame;
import org.bukkit.event.Event;

public abstract class ZombiesEvent extends Event {
    private final ZombiesGame game;

    protected ZombiesEvent(final ZombiesGame game) {
        this.game = game;
    }

    public ZombiesGame getGame() {
        return game;
    }
}
