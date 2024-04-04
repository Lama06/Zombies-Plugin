package io.lama06.zombies.util;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;

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

    public static FinePosition getMidpoint(final FinePosition p1, final FinePosition p2) {
        return Position.fine((p1.x() + p2.x()) / 2, (p1.y() + p2.y()) / 2, (p1.z() + p2.z()) / 2);
    }
}
