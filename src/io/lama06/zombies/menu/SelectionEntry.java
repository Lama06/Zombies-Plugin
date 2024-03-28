package io.lama06.zombies.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public record SelectionEntry(
        Component name,
        Material item,
        Runnable callback,
        Component secondActionDescription,
        Runnable secondAction
) {
    public SelectionEntry(final Component name, final Material item, final Runnable callback) {
        this(name, item, callback, null, null);
    }
}
