package io.lama06.zombies.event.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.HandlerListGetter;
import org.bukkit.event.HandlerList;

public final class PlayerGoldChangeEvent extends PlayerEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final int oldGold;
    private final int newGold;

    public PlayerGoldChangeEvent(final ZombiesPlayer player, final int oldGold, final int newGold) {
        super(player);
        this.oldGold = oldGold;
        this.newGold = newGold;
    }

    public int getOldGold() {
        return oldGold;
    }

    public int getNewGold() {
        return newGold;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
