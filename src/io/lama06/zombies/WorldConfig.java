package io.lama06.zombies;

import io.lama06.zombies.menu.*;
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
                                window -> Component.text("Window in:" + (window.area.isEmpty() ? "_" : window.area)),
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
                )
        );
    }
}
