package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public record FireAttackData(int ticks) {
    public static final AttributeId<Integer> TICKS = new AttributeId<>("ticks", PersistentDataType.INTEGER);
}
