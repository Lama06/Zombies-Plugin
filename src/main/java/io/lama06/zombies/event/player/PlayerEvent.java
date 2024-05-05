package io.lama06.zombies.event.player;

import io.lama06.zombies.event.ZombiesEvent;
import io.lama06.zombies.ZombiesPlayer;

public abstract class PlayerEvent extends ZombiesEvent {
    private final ZombiesPlayer player;

    public PlayerEvent(final ZombiesPlayer player) {
        super(player.getWorld());
        this.player = player;
    }

    public ZombiesPlayer getPlayer() {
        return player;
    }
}
