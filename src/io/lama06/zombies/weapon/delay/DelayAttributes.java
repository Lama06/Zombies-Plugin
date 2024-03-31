package io.lama06.zombies.weapon.delay;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public final class DelayAttributes {
    public static final AttributeId<Integer> DELAY = new AttributeId<>("delay", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> REMAINING_DELAY = new AttributeId<>("remaining_delay", PersistentDataType.INTEGER);
}
