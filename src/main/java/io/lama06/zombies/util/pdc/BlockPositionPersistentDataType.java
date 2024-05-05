package io.lama06.zombies.util.pdc;

import io.lama06.zombies.ZombiesPlugin;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class BlockPositionPersistentDataType implements PersistentDataType<PersistentDataContainer, BlockPosition> {
    public static final BlockPositionPersistentDataType INSTANCE = new BlockPositionPersistentDataType();

    private BlockPositionPersistentDataType() { }

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<BlockPosition> getComplexType() {
        return BlockPosition.class;
    }

    private static NamespacedKey key(final String name) {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, name);
    }

    @Override
    public PersistentDataContainer toPrimitive(final BlockPosition complex, final PersistentDataAdapterContext context) {
        final PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(key("x"), INTEGER, complex.blockX());
        container.set(key("y"), INTEGER, complex.blockY());
        container.set(key("z"), INTEGER, complex.blockZ());
        return container;
    }

    @Override
    public BlockPosition fromPrimitive(final PersistentDataContainer primitive, final PersistentDataAdapterContext context) {
        final Integer x = primitive.get(key("x"), INTEGER);
        final Integer y = primitive.get(key("y"), INTEGER);
        final Integer z = primitive.get(key("z"), INTEGER);
        if (x == null || y == null || z == null) {
            throw new IllegalStateException("coordinates incomplete");
        }
        return Position.block(x, y, z);
    }
}
