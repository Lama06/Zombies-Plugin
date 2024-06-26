package io.lama06.zombies.event.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.HandlerListGetter;
import org.bukkit.event.HandlerList;

public final class PlayerKillsIncrementEvent extends PlayerEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final int kills;

    public PlayerKillsIncrementEvent(final ZombiesPlayer player, final int kills) {
        super(player);
        this.kills = kills;
    }

    public int getKills() {
        return kills;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
