package io.lama06.zombies.util.pdc;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.util.UUID;

public final class UuidDataType implements PersistentDataType<byte[], UUID> {
    public static final UuidDataType INSTANCE = new UuidDataType();

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Override
    public byte[] toPrimitive(final UUID complex, final PersistentDataAdapterContext context) {
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(complex.getMostSignificantBits());
        buffer.putLong(complex.getLeastSignificantBits());
        return buffer.array();
    }

    @Override
    public UUID fromPrimitive(final byte[] primitive, final PersistentDataAdapterContext context) {
        final ByteBuffer buffer = ByteBuffer.wrap(primitive);
        final long mostSignificant = buffer.getLong();
        final long leastSignificant = buffer.getLong();
        return new UUID(mostSignificant, leastSignificant);
    }
}
