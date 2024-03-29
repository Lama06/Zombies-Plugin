package io.lama06.zombies.zombie.event;

import io.lama06.zombies.event.ZombiesEvent;
import io.lama06.zombies.zombie.Zombie;

public abstract class ZombieEvent extends ZombiesEvent {
    private final Zombie zombie;

    protected ZombieEvent(final Zombie zombie) {
        super(zombie.getGame());
        this.zombie = zombie;
    }

    public Zombie getZombie() {
        return zombie;
    }
}
