package io.lama06.zombies.util;

import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public record EntityPosition(FinePosition coordinates, float yaw, float pitch) {
    public static EntityPosition ofEntity(final Entity entity) {
        return new EntityPosition(Position.fine(entity.getLocation()), entity.getYaw(), entity.getPitch());
    }

    public Location toBukkit(final World world) {
        return new Location(world, coordinates.x(), coordinates.y(), coordinates.z(), yaw, pitch);
    }

    @Override
    public String toString() {
        return PositionUtil.format(coordinates);
    }
}
