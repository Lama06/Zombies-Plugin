package io.lama06.zombies.zombie.component;

import io.lama06.zombies.zombie.Zombie;

public abstract class ZombieComponent {
    protected final Zombie zombie;

    protected ZombieComponent(final Zombie zombie) {
        this.zombie = zombie;
    }

    public Zombie getZombie() {
        return zombie;
    }
}
