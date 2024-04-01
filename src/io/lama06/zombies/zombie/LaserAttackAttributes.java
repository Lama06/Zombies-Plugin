package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.util.json.UUIDTypeAdapter;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public final class LaserAttackAttributes {
    public static final AttributeId<Double> DAMAGE = new AttributeId<>("damage", PersistentDataType.DOUBLE);
    public static final AttributeId<UUID> GUARDIAN = new AttributeId<>("guardian", UUIDTypeAdapter.INSTANCE);
}
