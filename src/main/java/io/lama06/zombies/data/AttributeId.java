package io.lama06.zombies.data;

import io.lama06.zombies.ZombiesPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

public record AttributeId<T>(String name, PersistentDataType<?, T> type) {
    public NamespacedKey getKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, name);
    }
}
