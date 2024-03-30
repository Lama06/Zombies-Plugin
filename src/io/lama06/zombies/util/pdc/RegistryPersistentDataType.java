package io.lama06.zombies.util.pdc;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class RegistryPersistentDataType<T extends Keyed> implements PersistentDataType<String, T> {
    private final Class<T> complexType;
    private final Registry<T> registry;

    public RegistryPersistentDataType(final Class<T> complexType, final Registry<T> registry) {
        this.complexType = complexType;
        this.registry = registry;
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<T> getComplexType() {
        return complexType;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull final T complex, @NotNull final PersistentDataAdapterContext context) {
        return complex.getKey().toString();
    }

    @Override
    public @NotNull T fromPrimitive(
            @NotNull final String primitive, @NotNull final PersistentDataAdapterContext context
    ) {
        final NamespacedKey key = NamespacedKey.fromString(primitive);
        if (key == null) {
            throw new IllegalStateException("invalid key");
        }
        final T entry = registry.get(key);
        if (entry == null) {
            throw new IllegalStateException("nonexistent key");
        }
        return entry;
    }
}
