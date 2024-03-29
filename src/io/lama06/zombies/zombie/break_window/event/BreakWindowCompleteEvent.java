package io.lama06.zombies.zombie.break_window.event;

import io.lama06.zombies.zombie.Zombie;

public final class BreakWindowCompleteEvent extends BreakWindowEvent {
    public BreakWindowCompleteEvent(final Zombie zombie) {
        super(zombie);
    }
}
