package io.lama06.zombies.weapon;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public final class AttackAttributes {
    public static final AttributeId<Double> DAMAGE = new AttributeId<>("damage", PersistentDataType.DOUBLE);
    public static final AttributeId<Boolean> FIRE = new AttributeId<>("fire", PersistentDataType.BOOLEAN);
    public static final AttributeId<Integer> GOLD = new AttributeId<>("gold", PersistentDataType.INTEGER);
}
