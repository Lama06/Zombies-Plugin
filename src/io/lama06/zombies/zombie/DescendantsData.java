package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.util.pdc.EnumPersistentDataType;
import org.bukkit.persistence.PersistentDataType;

public record DescendantsData(ZombieType type, int count) {
    public static final AttributeId<ZombieType> TYPE = new AttributeId<>(
            "type",
            new EnumPersistentDataType<>(ZombieType.class)
    );
    public static final AttributeId<Integer> COUNT = new AttributeId<>("count", PersistentDataType.INTEGER);
}
