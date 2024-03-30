package io.lama06.zombies;

import org.bukkit.NamespacedKey;

public record Attribute(String name) {
    public NamespacedKey getKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, name);
    }
}
