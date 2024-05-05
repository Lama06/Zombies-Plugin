package io.lama06.zombies.util.pdc;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public final class EnumPersistentDataType<T extends Enum<T>> implements PersistentDataType<String, T> {
    private final Class<T> type;

    public EnumPersistentDataType(final Class<T> type) {
        this.type = type;
    }

    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public Class<T> getComplexType() {
        return type;
    }

    @Override
    public String toPrimitive(final T complex, final PersistentDataAdapterContext context) {
        return complex.name();
    }

    @Override
    public T fromPrimitive(final String primitive, final PersistentDataAdapterContext context) {
        return Enum.valueOf(type, primitive);
    }
}
