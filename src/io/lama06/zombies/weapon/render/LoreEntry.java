package io.lama06.zombies.weapon.render;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public record LoreEntry(String name, String value) {
    public Component toComponent() {
        return Component.text(name)
                .append(Component.text(": "))
                .append(Component.text(value).color(NamedTextColor.GREEN));
    }
}
