package io.lama06.zombies.weapon;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public record ReloadData(int reload) {
    public static final AttributeId<Integer> REMAINING_RELOAD = new AttributeId<>("remaining_reload", PersistentDataType.INTEGER);
}
