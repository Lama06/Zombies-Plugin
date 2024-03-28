package io.lama06.zombies.zombie.event;

import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.Event;

public abstract class ZombieEvent extends Event {
    private final Zombie zombie;

    protected ZombieEvent(final Zombie zombie) {
        this.zombie = zombie;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public ZombiesGame getGame() {
        return zombie.getGame();
    }
}
