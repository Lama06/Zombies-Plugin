package io.lama06.zombies;

import io.lama06.zombies.menu.*;
import io.lama06.zombies.perk.PerkMachine;
import io.lama06.zombies.util.PositionUtil;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class WorldConfig {
    public String startArea = "";
    public final List<Door> doors = new ArrayList<>();
    public PowerSwitch powerSwitch = new PowerSwitch();
    public final List<Window> windows = new ArrayList<>();
    public final List<WeaponShop> weaponShops = new ArrayList<>();
    public final List<ArmorShop> armorShops = new ArrayList<>();
    public final List<LuckyChest> luckyChests = new ArrayList<>();
    public boolean preventBuilding;
    public BlockPosition teamMachine;
    public final List<PerkMachine> perkMachines = new ArrayList<>();

    public void check() throws InvalidConfigException {
        InvalidConfigException.mustBeSet(startArea, "start area");
        for (final Door door : doors) {
            try {
                door.check();
            } catch (final InvalidConfigException e) {
                throw new InvalidConfigException("door invalid", e);
            }
        }
        if (windows.isEmpty()) {
            throw new InvalidConfigException("no windows");
        }
        for (final Window window : windows) {
            try {
                window.check();
            } catch (final InvalidConfigException e) {
                throw new InvalidConfigException("window invalid", e);
            }
        }
    }

    public void openMenu(final Player player, final Runnable callback) {
        final Runnable reopen = () -> openMenu(player, callback);

        SelectionMenu.open(
                player,
                Component.text("Zombies World Configuration"),
                callback,
                new SelectionEntry(
                        Component.text("Start Area: " + (startArea.isEmpty() ? "_" : startArea)),
                        Material.OAK_FENCE,
                        () -> InputMenu.open(
                                player,
                                Component.text("Start Area"),
                                startArea,
                                new TextInputType(),
                                startArea -> {
                                    this.startArea = startArea;
                                    reopen.run();
                                },
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Doors"),
                        Material.ACACIA_DOOR,
                        () -> ListConfigMenu.open(
                                player,
                                Component.text("Doors"),
                                doors,
                                Material.ACACIA_DOOR,
                                door -> Component.text("Door from %s to %s".formatted(door.area1.isEmpty() ? "_" : door.area1, door.area2.isEmpty() ? "_" : door.area2)),
                                Door::new,
                                door -> door.openMenu(player, reopen),
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Windows"),
                        Material.GLASS,
                        () -> ListConfigMenu.open(
                                player,
                                Component.text("Windows"),
                                windows,
                                Material.ACACIA_DOOR,
                                window -> Component.text("Window in: " + (window.area.isEmpty() ? "_" : window.area)),
                                Window::new,
                                window -> window.openMenu(player, reopen),
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Power Switch" + (powerSwitch == null ? ": null" : "")),
                        Material.LEVER,
                        () -> (powerSwitch != null ? powerSwitch : (powerSwitch = new PowerSwitch())).openMenu(player, reopen),
                        Component.text("Remove").color(NamedTextColor.RED),
                        () -> {
                            powerSwitch = null;
                            reopen.run();
                        }
                ),
                new SelectionEntry(
                        Component.text("Weapon Shops"),
                        Material.WOODEN_HOE,
                        () -> ListConfigMenu.open(
                                player,
                                Component.text("Weapon Shops"),
                                weaponShops,
                                Material.WOODEN_HOE,
                                shop -> Component.text("Weapon Shop at " + PositionUtil.format(shop.position)),
                                WeaponShop::new,
                                shop -> shop.openMenu(player, reopen),
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Armor Shops"),
                        Material.IRON_CHESTPLATE,
                        () -> ListConfigMenu.open(
                                player,
                                Component.text("Armor Shops"),
                                armorShops,
                                Material.IRON_CHESTPLATE,
                                shop -> Component.text("Armor Shop at " + PositionUtil.format(shop.position)),
                                ArmorShop::new,
                                shop -> shop.openMenu(player, reopen),
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Lucky Chests"),
                        Material.CHEST,
                        () -> ListConfigMenu.open(
                                player,
                                Component.text("Lucky Chests"),
                                luckyChests,
                                Material.CHEST,
                                luckyChest -> Component.text("Lucky Chest at " + PositionUtil.format(luckyChest.position)),
                                LuckyChest::new,
                                luckChest -> luckChest.openMenu(player, reopen),
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Prevent Building by Operators: " + preventBuilding),
                        Material.BARRIER,
                        () -> {
                            preventBuilding = !preventBuilding;
                            reopen.run();
                        }
                ),
                new SelectionEntry(
                        Component.text("Team Machine: " + PositionUtil.format(teamMachine)),
                        Material.IRON_BLOCK,
                        () -> BlockPositionSelection.open(
                                player,
                                Component.text("Team Machine Position"),
                                reopen,
                                teamMachine -> {
                                    this.teamMachine = teamMachine;
                                    reopen.run();
                                }
                        ),
                        Component.text("Remove").color(NamedTextColor.RED),
                        () -> {
                            teamMachine = null;
                            reopen.run();
                        }
                ),
                new SelectionEntry(
                        Component.text("Perk Machines"),
                        Material.COMMAND_BLOCK,
                        () -> ListConfigMenu.open(
                                player,
                                Component.text("Perk Machines"),
                                perkMachines,
                                Material.COMMAND_BLOCK,
                                machine -> Component.text("Perk Machine at " + PositionUtil.format(machine.position)),
                                PerkMachine::new,
                                machine -> machine.openMenu(player, reopen),
                                reopen
                        )
                )
        );
    }
}
