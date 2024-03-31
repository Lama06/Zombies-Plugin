package io.lama06.zombies.player;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public final class PlayerAttributes {
    public static final AttributeId<Integer> GOLD = new AttributeId<>("gold", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> KILLS = new AttributeId<>("kills", PersistentDataType.INTEGER);
}
