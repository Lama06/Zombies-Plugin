package io.lama06.zombies.weapon;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public record AmmoData(int ammo, int clip) {
    public static final AttributeId<Integer> MAX_AMMO = new AttributeId<>("max_ammo", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> AMMO = new AttributeId<>("ammo", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> MAX_CLIP = new AttributeId<>("max_clip", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> CLIP = new AttributeId<>("clip", PersistentDataType.INTEGER);
}
