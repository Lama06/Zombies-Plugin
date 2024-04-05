package io.lama06.zombies;

import io.lama06.zombies.menu.*;
import io.lama06.zombies.util.PositionUtil;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;

public final class LuckyChest implements CheckableConfig {
    public BlockPosition position;
    public int gold = 1000;

    @Override
    public void check() throws InvalidConfigException {
        InvalidConfigException.mustBeSet(position, "position");
    }

    public Block getSecondChestBlock(final World world) {
        final Block block = position.toLocation(world).getBlock();
        final BlockData blockData = block.getBlockData();
        if (!(blockData instanceof final Chest chest)) {
            return null;
        }
        final Block secondChestBlock = switch (chest.getType()) {
            case SINGLE -> null;
            case RIGHT -> switch (chest.getFacing()) {
                case NORTH -> block.getRelative(BlockFace.WEST);
                case WEST -> block.getRelative(BlockFace.SOUTH);
                case SOUTH -> block.getRelative(BlockFace.EAST);
                case EAST -> block.getRelative(BlockFace.NORTH);
                default -> null;
            };
            case LEFT -> switch (chest.getFacing()) {
                case NORTH -> block.getRelative(BlockFace.EAST);
                case EAST -> block.getRelative(BlockFace.SOUTH);
                case SOUTH -> block.getRelative(BlockFace.WEST);
                case WEST -> block.getRelative(BlockFace.NORTH);
                default -> null;
            };
        };
        if (secondChestBlock == null) {
            return null;
        }
        if (secondChestBlock.getType() != Material.CHEST) {
            return null;
        }
        return secondChestBlock;
    }

    public FinePosition getItemPosition(final World world) {
        final Block secondChestBlock = getSecondChestBlock(world);
        if (secondChestBlock == null) {
            return position.toCenter().offset(0, 1, 0);
        }
        return PositionUtil.getMidpoint(position.toCenter(), secondChestBlock.getLocation().toCenter()).offset(0, 1, 0);
    }

    public void openMenu(final Player player, final Runnable callback) {
        final Runnable reopen = () -> openMenu(player, callback);
        SelectionMenu.open(
                player,
                Component.text("Lucky Chest"),
                callback,
                new SelectionEntry(
                        Component.text("Position: " + PositionUtil.format(position)),
                        Material.CHEST,
                        () -> BlockPositionSelection.open(
                                player,
                                Component.text("Lucky Chest Position"),
                                reopen,
                                position -> {
                                    this.position = position;
                                    reopen.run();
                                }
                        )
                ),
                new SelectionEntry(
                        Component.text("Gold: " + gold),
                        Material.GOLD_NUGGET,
                        () -> InputMenu.open(
                                player,
                                Component.text("Lucky Chest Price"),
                                gold,
                                new IntegerInputType(),
                                gold -> {
                                    this.gold = gold;
                                    reopen.run();
                                },
                                reopen
                        )
                )
        );
    }
}
