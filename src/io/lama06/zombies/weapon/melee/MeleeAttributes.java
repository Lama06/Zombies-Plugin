package io.lama06.zombies.weapon.melee;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public final class MeleeAttributes {
    public static final AttributeId<Double> RANGE = new AttributeId<>("range", PersistentDataType.DOUBLE);
}
