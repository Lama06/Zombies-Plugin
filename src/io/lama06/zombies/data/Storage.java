package io.lama06.zombies.data;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public abstract class Storage {
    protected abstract StorageSession startSession();

    public final boolean hasComponent(final ComponentId component) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        return data.has(component.getKey(), PersistentDataType.TAG_CONTAINER);
    }

    public final Component getComponent(final ComponentId component) {
        if (!hasComponent(component)) {
            return null;
        }
        return new Component(this, component);
    }

    public Component addComponent(final ComponentId component) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        final PersistentDataContainer container = data.getAdapterContext().newPersistentDataContainer();
        data.set(component.getKey(), PersistentDataType.TAG_CONTAINER, container);
        session.applyChanges();
        return Objects.requireNonNull(getComponent(component));
    }

    public void removeComponent(final ComponentId component) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        data.remove(component.getKey());
        session.applyChanges();
    }

    public final <T> T get(final AttributeId<T> attribute) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        return data.get(attribute.getKey(), attribute.type());
    }

    public final <T> void set(final AttributeId<T> attribute, final T value) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        data.set(attribute.getKey(), attribute.type(), value);
        session.applyChanges();
    }

    public final void remove(final AttributeId<?> attribute) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        data.remove(attribute.getKey());
        session.applyChanges();
    }
}
