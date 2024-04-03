package io.lama06.zombies.zombie;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.data.ComponentId;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import io.lama06.zombies.util.pdc.EnumPersistentDataType;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public final class Zombie extends Storage {
    public static final AttributeId<Boolean> IS_ZOMBIE = new AttributeId<>("is_zombie", PersistentDataType.BOOLEAN);
    public static final AttributeId<ZombieType> TYPE = new AttributeId<>("type", new EnumPersistentDataType<>(ZombieType.class));

    public static final ComponentId BREAK_WINDOW = new ComponentId("break_window");
    public static final ComponentId LASER_ATTACK = new ComponentId("laser_attack");

    private final Entity entity;

    public Zombie(final Entity entity) {
        this.entity = entity;
    }

    public ZombieType getType() {
        return get(TYPE);
    }

    public ZombieData getData() {
        return getType().data;
    }

    public boolean isZombie() {
        return getOrDefault(IS_ZOMBIE, false);
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
