package io.lama06.zombies.util;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.util.HashSet;
import java.util.Set;

public record BlockArea(BlockPosition position1, BlockPosition position2) {
    public BlockArea {
        if (position1 == null || position2 == null) {
            throw new NullPointerException();
        }
    }

    public int getUpperX() {
        return Math.max(position1.blockX(), position2.blockX());
    }

    public int getUpperY() {
        return Math.max(position1.blockY(), position2.blockY());
    }

    public int getUpperZ() {
        return Math.max(position1.blockZ(), position2.blockZ());
    }

    public int getLowerX() {
        return Math.min(position1.blockX(), position2.blockX());
    }

    public int getLowerY() {
        return Math.min(position1.blockY(), position2.blockY());
    }

    public int getLowerZ() {
        return Math.min(position1.blockZ(), position2.blockZ());
    }

    public BlockPosition getLowerCorner() {
        return Position.block(getLowerX(), getLowerY(), getLowerZ());
    }

    public BlockPosition getUpperCorner() {
        return Position.block(getUpperX(), getUpperY(), getUpperZ());
    }

    public Set<BlockPosition> getBlocks() {
        final Set<BlockPosition> blocks = new HashSet<>();

        final BlockPosition lowerCorner = getLowerCorner();
        final BlockPosition upperCorner = getUpperCorner();

        for (int x = lowerCorner.blockX(); x <= upperCorner.blockX(); x++) {
            for (int y = lowerCorner.blockY(); y <= upperCorner.blockY(); y++) {
                for (int z = lowerCorner.blockZ(); z <= upperCorner.blockZ(); z++) {
                    blocks.add(Position.block(x, y, z));
                }
            }
        }

        return blocks;
    }

    public boolean containsBlock(final BlockPosition position) {
        final BlockPosition lowerCorner = getLowerCorner();
        final BlockPosition upperCorner = getUpperCorner();

        final boolean x = position.blockX() >= lowerCorner.blockX() && position.blockX() <= upperCorner.blockX();
        final boolean y = position.blockY() >= lowerCorner.blockY() && position.blockY() <= upperCorner.blockY();
        final boolean z = position.blockZ() >= lowerCorner.blockZ() && position.blockZ() <= upperCorner.blockZ();

        return x && y && z;
    }

    public void clone(final World world, final BlockArea destination) {
        if (!hasSameDimensions(destination)) {
            return;
        }

        final BlockPosition sourceLowerCorner = getLowerCorner();
        final BlockPosition sourceUpperCorner = getUpperCorner();
        final BlockPosition destinationLowerCorner = destination.getLowerCorner();

        for (int x = sourceLowerCorner.blockX(); x <= sourceUpperCorner.blockX(); x++) {
            for (int y = sourceLowerCorner.blockY(); y <= sourceUpperCorner.blockY(); y++) {
                for (int z = sourceLowerCorner.blockZ(); z <= sourceUpperCorner.blockZ(); z++) {
                    final int xOffset = x - sourceLowerCorner.blockX();
                    final int yOffset = y - sourceLowerCorner.blockY();
                    final int zOffset = z - sourceLowerCorner.blockZ();

                    world.getBlockAt(
                            destinationLowerCorner.blockX() + xOffset,
                            destinationLowerCorner.blockY() + yOffset,
                            destinationLowerCorner.blockZ() + zOffset
                    ).setBlockData(world.getBlockData(x, y, z));
                }
            }
        }
    }

    public void fill(final World world, final BlockData data) {
        for (final var block : getBlocks()) {
            block.toLocation(world).getBlock().setBlockData(data);
        }
    }

    public int getHeight() {
        return getUpperX() - getLowerX() + 1;
    }

    public int getWidthX() {
        return getUpperY() - getLowerY() + 1;
    }

    public int getWidthZ() {
        return getUpperZ() - getLowerZ() + 1;
    }

    public boolean hasSameDimensions(final BlockArea other) {
        return getHeight() == other.getHeight() && getWidthX() == other.getWidthX() && getWidthZ() == other.getWidthZ();
    }

    public boolean is2d() {
        return getHeight() == 1 || getWidthX() == 1 || getLowerZ() == 1;
    }

    @Override
    public String toString() {
        return PositionUtil.format(position1) + " - " + PositionUtil.format(position2);
    }
}