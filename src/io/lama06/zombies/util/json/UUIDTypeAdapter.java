package io.lama06.zombies.util.json;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.UUID;

public final class UUIDTypeAdapter implements PersistentDataType<byte[], UUID> {
    public static final UUIDTypeAdapter INSTANCE = new UUIDTypeAdapter();

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(
            @NotNull final UUID complex, @NotNull final PersistentDataAdapterContext context
    ) {
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(complex.getMostSignificantBits());
        buffer.putLong(complex.getLeastSignificantBits());
        return buffer.array();
    }

    @Override
    public @NotNull UUID fromPrimitive(
            final byte @NotNull [] primitive, @NotNull final PersistentDataAdapterContext context
    ) {
        final ByteBuffer buffer = ByteBuffer.wrap(primitive);
        final long mostSignificant = buffer.getLong();
        final long leastSignificant = buffer.getLong();
        return new UUID(mostSignificant, leastSignificant);
    }
}
