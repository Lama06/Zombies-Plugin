package io.lama06.zombies.zombie.event;

import io.lama06.zombies.event.ZombiesEvent;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;

public abstract class ZombieEvent extends ZombiesEvent {
    private final Entity zombie;

    protected ZombieEvent(final Entity zombie) {
        super(zombie.getWorld());
        this.zombie = zombie;
    }

    public Entity getZombie() {
        return zombie;
    }

    public PersistentDataContainer getPdc() {
        return zombie.getPersistentDataContainer();
    }
}
