package io.lama06.zombies.zombie.break_window.event;

import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.Cancellable;

public final class BreakWindowTickEvent extends BreakWindowEvent implements Cancellable {
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
}
