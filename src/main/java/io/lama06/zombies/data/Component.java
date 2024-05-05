package io.lama06.zombies.data;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class Component extends Storage {
    private final Storage parent;
    private final ComponentId componentId;

    public Component(final Storage parent, final ComponentId componentId) {
        this.parent = parent;
        this.componentId = componentId;
    }

    @Override
    protected StorageSession startSession() {
        final StorageSession session = parent.startSession();
        final PersistentDataContainer data = session.getData();
        final PersistentDataContainer container = data.get(componentId.getKey(), PersistentDataType.TAG_CONTAINER);
        return new StorageSession() {
            @Override
            public PersistentDataContainer getData() {
                return container;
            }

            @Override
            public void applyChanges() {
                data.set(componentId.getKey(), PersistentDataType.TAG_CONTAINER, container);
                session.applyChanges();
            }
        };
    }
}
