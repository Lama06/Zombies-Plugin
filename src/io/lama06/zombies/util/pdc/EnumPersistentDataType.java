package io.lama06.zombies.util.pdc;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class EnumPersistentDataType<T extends Enum<T>> implements PersistentDataType<String, T> {
    private final Class<T> type;

    public EnumPersistentDataType(final Class<T> type) {
        this.type = type;
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<T> getComplexType() {
        return type;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull final T complex, @NotNull final PersistentDataAdapterContext context) {
        return complex.name();
    }

    @Override
    public @NotNull T fromPrimitive(
            @NotNull final String primitive, @NotNull final PersistentDataAdapterContext context
    ) {
        return Enum.valueOf(type, primitive);
    }
}
