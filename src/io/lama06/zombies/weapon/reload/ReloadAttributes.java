package io.lama06.zombies.weapon.reload;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public final class ReloadAttributes {
    public static final AttributeId<Integer> RELOAD = new AttributeId<>("reload", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> REMAINING_RELOAD = new AttributeId<>("remaining_reload", PersistentDataType.INTEGER);
}
