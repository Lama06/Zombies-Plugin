package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.util.pdc.UuidDataType;

import java.util.UUID;

public record LaserAttackData(double damage) {
    public static final AttributeId<UUID> GUARDIAN = new AttributeId<>("guardian", UuidDataType.INSTANCE);
}
