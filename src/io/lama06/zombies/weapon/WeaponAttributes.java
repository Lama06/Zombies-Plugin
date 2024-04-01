package io.lama06.zombies.weapon;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.util.pdc.EnumPersistentDataType;
import org.bukkit.persistence.PersistentDataType;

public final class WeaponAttributes {
    public static final AttributeId<Boolean> IS_WEAPON = new AttributeId<>("is_weapon", PersistentDataType.BOOLEAN);
    public static final AttributeId<WeaponType> TYPE = new AttributeId<>("type", new EnumPersistentDataType<>(WeaponType.class));
}
