package io.lama06.zombies.zombie;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class Zombie extends Storage {
    public static final AttributeId<Boolean> IS_ZOMBIE = new AttributeId<>("is_zombie", PersistentDataType.BOOLEAN);

    public static boolean isZombie(final Entity entity) {
        if (entity == null) {
            return false;
        }
        final PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.getOrDefault(IS_ZOMBIE.getKey(), PersistentDataType.BOOLEAN, false);
    }

    private final Entity entity;

    public Zombie(final Entity entity) {
        this.entity = entity;
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
