package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public record FireBallAttackData(double damage, int delay) {
    public static final AttributeId<Double> DAMAGE = new AttributeId<>("damage", PersistentDataType.DOUBLE);
    public static final AttributeId<Integer> DELAY = new AttributeId<>("delay", PersistentDataType.INTEGER);
}
