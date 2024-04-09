package io.lama06.zombies.event.zombie;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public final class BreakWindowTickEvent extends ZombieEvent implements Cancellable {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final int newRemainingTime;
    private boolean canceled;

    public BreakWindowTickEvent(final Zombie zombie, final int newRemainingTime) {
        super(zombie);
        this.newRemainingTime = newRemainingTime;
    }

    public int getNewRemainingTime() {
        return newRemainingTime;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        canceled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
