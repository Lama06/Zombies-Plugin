package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public final class ZombieAttributes {
    public static final AttributeId<Boolean> IS_ZOMBIE = new AttributeId<>("is_zombie", PersistentDataType.BOOLEAN);
}
