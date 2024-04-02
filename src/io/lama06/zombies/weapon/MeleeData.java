package io.lama06.zombies.weapon;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public record MeleeData(double range) {
    public static final AttributeId<Double> RANGE = new AttributeId<>("range", PersistentDataType.DOUBLE);
}
