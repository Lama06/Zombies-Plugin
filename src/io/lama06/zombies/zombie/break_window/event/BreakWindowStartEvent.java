package io.lama06.zombies.zombie.break_window.event;

import io.lama06.zombies.zombie.Zombie;

public final class BreakWindowStartEvent extends BreakWindowEvent {
    public BreakWindowStartEvent(final Zombie zombie) {
        super(zombie);
    }
}
