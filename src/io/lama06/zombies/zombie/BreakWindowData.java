package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.util.pdc.BlockPositionPersistentDataType;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.persistence.PersistentDataType;

public record BreakWindowData(int time, double maxDistance) {
    public BreakWindowData(final int time) {
        this(time, 1);
    }

    public static final AttributeId<Integer> REMAINING_TIME = new AttributeId<>("remaining_time", PersistentDataType.INTEGER);
    public static final AttributeId<BlockPosition> BLOCK = new AttributeId<>("block", BlockPositionPersistentDataType.INSTANCE);
}
