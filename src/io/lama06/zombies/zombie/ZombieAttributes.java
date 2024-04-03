package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.util.pdc.EnumPersistentDataType;
import org.bukkit.persistence.PersistentDataType;

public final class ZombieAttributes {
    public static final AttributeId<Boolean> IS_ZOMBIE = new AttributeId<>("is_zombie", PersistentDataType.BOOLEAN);
    public static final AttributeId<ZombieType> TYPE = new AttributeId<>("type", new EnumPersistentDataType<>(ZombieType.class));
}
