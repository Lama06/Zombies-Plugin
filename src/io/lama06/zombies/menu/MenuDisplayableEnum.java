package io.lama06.zombies.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public interface MenuDisplayableEnum {
    Component getDisplayName();

    Material getDisplayMaterial();
}
