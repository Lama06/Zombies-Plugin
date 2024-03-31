package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.util.pdc.BlockPositionPersistentDataType;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.persistence.PersistentDataType;

public final class BreakWindowAttributes {
    public static final AttributeId<Integer> TIME = new AttributeId<>("time", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> REMAINING_TIME = new AttributeId<>("remaining_time", PersistentDataType.INTEGER);
    public static final AttributeId<Double> MAX_DISTANCE = new AttributeId<>("max_distance", PersistentDataType.DOUBLE);
    public static final AttributeId<BlockPosition> BLOCK = new AttributeId<>("block", BlockPositionPersistentDataType.INSTANCE);
}
