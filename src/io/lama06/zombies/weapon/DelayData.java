package io.lama06.zombies.weapon;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public record DelayData(int delay) {
    public static final AttributeId<Integer> DELAY = new AttributeId<>("delay", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> REMAINING_DELAY = new AttributeId<>("remaining_delay", PersistentDataType.INTEGER);
}
