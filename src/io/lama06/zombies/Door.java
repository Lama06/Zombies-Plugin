package io.lama06.zombies;

import io.lama06.zombies.menu.*;
import io.lama06.zombies.util.BlockArea;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class Door implements CheckableConfig {
    public String area1 = "";
    public String area2 = "";
    public int gold;
    public BlockArea position;
    public BlockArea templateOpen;
    public BlockArea templateClosed;

    public void setOpen(final ZombiesWorld world, final boolean open) {
        if (open && templateOpen == null) {
            position.fill(world.getBukkit(), Material.AIR.createBlockData());
            return;
        }
        (open ? templateOpen : templateClosed).clone(world.getBukkit(), position);
    }

    @Override
    public void check() throws InvalidConfigException {
        InvalidConfigException.mustBeSet(area1, "first area");
        InvalidConfigException.mustBeSet(area2, "second area");
        InvalidConfigException.mustBeSet(position, "position");
        InvalidConfigException.mustBeSet(templateClosed, "template closed");

        if (area1.equals(area2)) {
            throw new InvalidConfigException("the first area equals the second area");
        }
        if (!position.hasSameDimensions(templateClosed)) {
            throw new InvalidConfigException("closed template doesn't have the same dimensions as the door");
        }
        if (templateOpen != null && !position.hasSameDimensions(templateOpen)) {
            throw new InvalidConfigException("open template doesn't have the same dimensions as the door");
        }
    }

    public void openMenu(final Player player, final Runnable callback) {
        SelectionMenu.open(
                player,
                Component.text("Edit Door"),
                callback,
                new SelectionEntry(Component.text("First Area: " + area1), Material.ACACIA_DOOR, () -> InputMenu.open(
                        player,
                        Component.text("First Area"),
                        area1,
                        new TextInputType(),
                        area1 -> {
                            this.area1 = area1;
                            openMenu(player, callback);
                        },
                        () -> openMenu(player, callback)
                )),
                new SelectionEntry(Component.text("Second Area: " + area2), Material.SPRUCE_DOOR, () -> InputMenu.open(
                        player,
                        Component.text("Second Area"),
                        area2,
                        new TextInputType(),
                        area2 -> {
                            this.area2 = area2;
                            openMenu(player, callback);
                        },
                        () -> openMenu(player, callback)
                )),
                new SelectionEntry(Component.text("Gold: " + gold), Material.GOLD_NUGGET, () -> InputMenu.open(
                        player,
                        Component.text("Gold").color(NamedTextColor.GOLD),
                        gold,
                        new IntegerInputType(),
                        gold -> {
                            this.gold = gold;
                            openMenu(player, callback);
                        },
                        () -> openMenu(player, callback)
                )),
                new SelectionEntry(Component.text("Position: " + position), Material.CHERRY_DOOR, () -> BlockAreaSelection.open(
                        player,
                        Component.text("Door Position"),
                        callback,
                        position -> {
                            this.position = position;
                            openMenu(player, callback);
                        }
                )),
                new SelectionEntry(
                        Component.text("Template Open: " + templateOpen),
                        Material.STRUCTURE_BLOCK,
                        () -> BlockAreaSelection.open(
                                player,
                                Component.text("Template Open"),
                                callback,
                                templateOpen -> {
                                    this.templateOpen = templateOpen;
                                    openMenu(player, callback);
                                }
                        ),
                        Component.text("Reset"),
                        () -> {
                            templateClosed = null;
                            openMenu(player, callback);
                        }
                ),
                new SelectionEntry(
                        Component.text("Template Closed: " + templateClosed),
                        Material.STRUCTURE_BLOCK,
                        () -> BlockAreaSelection.open(
                                player,
                                Component.text("Template Closed"),
                                callback,
                                templateClosed -> {
                                    this.templateClosed = templateClosed;
                                    openMenu(player, callback);
                                }

                        )
                )
        );
    }
}
