package io.lama06.zombies.weapon;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public final class ShootAttributes {
    public static final AttributeId<Integer> BULLETS = new AttributeId<>("bullets", PersistentDataType.INTEGER);
    public static final AttributeId<Double> PRECISION = new AttributeId<>("precision", PersistentDataType.DOUBLE);
}
