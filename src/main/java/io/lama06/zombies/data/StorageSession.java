package io.lama06.zombies.data;

import org.bukkit.persistence.PersistentDataContainer;

@FunctionalInterface
public interface StorageSession {
    PersistentDataContainer getData();

    default void applyChanges() { }
}
