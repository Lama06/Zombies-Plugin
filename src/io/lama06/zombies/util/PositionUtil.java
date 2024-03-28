package io.lama06.zombies.util;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;

public final class PositionUtil {
    private PositionUtil() { }

    public static String format(final BlockPosition position) {
        if (position == null) {
            return "null";
        }
        return "%s %s %s".formatted(position.blockX(), position.blockY(), position.blockZ());
    }

    public static String format(final FinePosition position) {
        if (position == null) {
            return "null";
        }
        return "%s %s %s".formatted(position.x(), position.y(), position.z());
    }
}
