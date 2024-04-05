package io.lama06.zombies.perk;

import io.lama06.zombies.menu.*;
import io.lama06.zombies.util.PositionUtil;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class PerkMachine {
    public BlockPosition position;
    public PlayerPerk perk = PlayerPerk.EXTRA_HEALTH;
    public int gold = 500;

    public void openMenu(final Player player, final Runnable callback) {
        final Runnable reopen = () -> openMenu(player, callback);
        SelectionMenu.open(
                player,
                Component.text("Perk Machine"),
                callback,
                new SelectionEntry(
                        Component.text("Position: " + PositionUtil.format(position)),
                        Material.LEVER,
                        () -> BlockPositionSelection.open(
                                player,
                                Component.text("Perk Machine Position"),
                                reopen,
                                position -> {
                                    this.position = position;
                                    reopen.run();
                                }
                        )
                ),
                new SelectionEntry(
                        Component.text("Perk: ").append(perk.getDisplayName()),
                        perk.getDisplayMaterial(),
                        () -> EnumSelectionMenu.open(
                                PlayerPerk.class,
                                player,
                                Component.text("Perk"),
                                reopen,
                                perk -> {
                                    this.perk = perk;
                                    reopen.run();
                                }
                        )
                ),
                new SelectionEntry(
                        Component.text("Gold: " + gold).color(NamedTextColor.GOLD),
                        Material.GOLD_NUGGET,
                        () -> InputMenu.open(
                                player,
                                Component.text("Perk Machine Price").color(NamedTextColor.GOLD),
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
