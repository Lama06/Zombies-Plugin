package io.lama06.zombies;

import io.lama06.zombies.menu.*;
import io.lama06.zombies.util.PositionUtil;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;

public final class PowerSwitch {
    public int gold;
    public BlockPosition position;

    public void setActive(final ZombiesWorld world, final boolean active) {
        final Block block = position.toLocation(world.getBukkit()).getBlock();
        if (block.getType() != Material.LEVER) {
            return;
        }
        final Switch lever = (Switch) block.getBlockData();
        lever.setPowered(active);
    }

    public void openMenu(final Player player, final Runnable callback) {
        final Runnable reopen = () -> openMenu(player, callback);
        SelectionMenu.open(
                player,
                Component.text("Power Switch"),
                callback,
                new SelectionEntry(
                        Component.text("Gold: " + gold).color(NamedTextColor.GOLD),
                        Material.GOLD_NUGGET,
                        () -> InputMenu.open(
                                player,
                                Component.text("Gold").color(NamedTextColor.GOLD),
                                gold,
                                new IntegerInputType(),
                                gold -> {
                                    this.gold = gold;
                                    reopen.run();
                                },
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Position :" + PositionUtil.format(position)),
                        Material.LEVER,
                        () -> BlockPositionSelection.open(
                                player,
                                Component.text("Power Switch Position"),
                                reopen,
                                position -> {
                                    this.position = position;
                                    reopen.run();
                                }
                        )
                )
        );
    }
}
