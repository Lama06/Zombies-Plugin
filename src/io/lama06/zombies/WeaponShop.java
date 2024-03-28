package io.lama06.zombies;

import io.lama06.zombies.menu.*;
import io.lama06.zombies.weapon.WeaponType;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class WeaponShop {
    public BlockPosition position;
    public WeaponType weaponType;
    public int purchasePrice;
    public int refillPrice;

    public void openMenu(final Player player, final Runnable callback) {
        final Runnable reopen = () -> openMenu(player, callback);
        SelectionMenu.open(
                player,
                Component.text("Weapon Shop"),
                callback,
                new SelectionEntry(
                        Component.text("Position"),
                        Material.LEVER,
                        () -> BlockPositionSelection.open(
                                player,
                                Component.text("Weapon Shop Position"),
                                reopen,
                                position -> {
                                    this.position = position;
                                    reopen.run();
                                }
                        )
                ),
                new SelectionEntry(
                        Component.text("Weapon"),
                        Material.STONE_SWORD,
                        () -> EnumSelectionMenu.open(
                                WeaponType.class,
                                player,
                                Component.text("Sold Weapon"),
                                reopen,
                                weaponType -> {
                                    this.weaponType = weaponType;
                                    reopen.run();
                                }
                        )
                ),
                new SelectionEntry(
                        Component.text("Purchase Price"),
                        Material.GOLD_NUGGET,
                        () -> InputMenu.open(
                                player,
                                Component.text("Weapon Purchase Price"),
                                purchasePrice,
                                new IntegerInputType(),
                                purchasePrice -> {
                                    this.purchasePrice = purchasePrice;
                                    reopen.run();
                                },
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Refill Price"),
                        Material.GOLD_NUGGET,
                        () -> InputMenu.open(
                                player,
                                Component.text("Weapon Refill Price"),
                                refillPrice,
                                new IntegerInputType(),
                                refillPrice -> {
                                    this.refillPrice = refillPrice;
                                    reopen.run();
                                },
                                reopen
                        )
                )
        );
    }
}
