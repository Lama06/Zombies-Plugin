package io.lama06.zombies.data;

import io.lama06.zombies.ZombiesPlugin;
import org.bukkit.NamespacedKey;

public record ComponentId(String name) {
    public NamespacedKey getKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, name);
    }
}
