package io.lama06.zombies.zombie;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import org.bukkit.entity.Entity;

public final class Zombie extends Storage {
    private final Entity entity;

    public Zombie(final Entity entity) {
        this.entity = entity;
    }

    public boolean isZombie() {
        return getOrDefault(ZombieAttributes.IS_ZOMBIE, false);
    }

    public Entity getEntity() {
        return entity;
    }

    public ZombiesWorld getWorld() {
        return new ZombiesWorld(entity.getWorld());
    }

    @Override
    protected StorageSession startSession() {
        return entity::getPersistentDataContainer;
    }
}
