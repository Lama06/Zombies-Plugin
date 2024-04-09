package io.lama06.zombies.event.player;

import io.lama06.zombies.util.HandlerListGetter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public final class PlayerCancelCommandEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerCancelCommandEvent(final Player player) {
        super(player);
    }

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
