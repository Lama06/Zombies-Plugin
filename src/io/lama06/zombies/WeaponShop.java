package io.lama06.zombies;

import io.lama06.zombies.menu.*;
import io.lama06.zombies.util.PositionUtil;
import io.lama06.zombies.weapon.WeaponType;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class WeaponShop implements CheckableConfig {
    public BlockPosition position;
    public WeaponType weaponType = WeaponType.RIFLE;
    public int purchasePrice = 500;
    public int refillPrice = 250;

    @Override
    public void check() throws InvalidConfigException {
        InvalidConfigException.mustBeSet(position, "position");
        InvalidConfigException.mustBeSet(weaponType, "weapon type");
    }

    public void openMenu(final Player player, final Runnable callback) {
        final Runnable reopen = () -> openMenu(player, callback);
        SelectionMenu.open(
                player,
                Component.text("Weapon Shop"),
                callback,
                new SelectionEntry(
                        Component.text("Position: " + PositionUtil.format(position)),
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
                        Component.text("Weapon: ").append(weaponType.getDisplayName()),
                        weaponType.getDisplayMaterial(),
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
                        Component.text("Purchase Price: " + purchasePrice),
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
                        Component.text("Refill Price: " + refillPrice),
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
