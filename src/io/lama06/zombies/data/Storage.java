package io.lama06.zombies.data;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class Storage {
    protected abstract StorageSession startSession();

    public boolean hasComponent(final ComponentId component) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        return data.has(component.getKey(), PersistentDataType.TAG_CONTAINER);
    }

    public @Nullable Component getComponent(final ComponentId component) {
        if (!hasComponent(component)) {
            return null;
        }
        return new Component() {
            @Override
            public <T> T getOrDefault(final AttributeId<T> attribute, final T fallback) {
                return Storage.this.getOrDefault(component, attribute, fallback);
            }

            @Override
            public <T> T get(final AttributeId<T> attribute) {
                return Storage.this.get(component, attribute);
            }

            @Override
            public <T> void set(final AttributeId<T> attribute, final T data) {
                Storage.this.set(component, attribute, data);
            }

            @Override
            public void remove(final AttributeId<?> attribute) {
                Storage.this.remove(component, attribute);
            }
        };
    }

    public @NotNull Component addComponent(final ComponentId component) {
        if (hasComponent(component)) {
            throw new IllegalStateException();
        }
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        final PersistentDataContainer container = data.getAdapterContext().newPersistentDataContainer();
        data.set(component.getKey(), PersistentDataType.TAG_CONTAINER, container);
        session.applyChanges();
        return Objects.requireNonNull(getComponent(component));
    }

    public void removeComponent(final ComponentId component) {
        if (!hasComponent(component)) {
            throw new IllegalStateException();
        }
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        data.remove(component.getKey());
        session.applyChanges();
    }

    public <T> T getOrDefault(final AttributeId<T> attribute, final T fallback) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        final T value = data.get(attribute.getKey(), attribute.type());
        if (value == null) {
            return fallback;
        }
        return value;
    }

    public <T> T getOrDefault(final ComponentId component, final AttributeId<T> attribute, final T fallback) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        final PersistentDataContainer container = data.get(component.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            throw new IllegalStateException();
        }
        final T value = container.get(attribute.getKey(), attribute.type());
        if (value == null) {
            return fallback;
        }
        return value;
    }

    public <T> T get(final AttributeId<T> attribute) {
        final T data = getOrDefault(attribute, null);
        if (data == null) {
            throw new IllegalStateException();
        }
        return data;
    }

    public <T> T get(final ComponentId component, final AttributeId<T> attribute) {
        final T data = getOrDefault(component, attribute, null);
        if (data == null) {
            throw new IllegalStateException();
        }
        return data;
    }

    public <T> void set(final AttributeId<T> attribute, final T value) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        data.set(attribute.getKey(), attribute.type(), value);
        session.applyChanges();
    }

    public <T> void set(final ComponentId component, final AttributeId<T> attribute, final T value) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        final PersistentDataContainer container = data.get(component.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            throw new IllegalStateException();
        }
        container.set(attribute.getKey(), attribute.type(), value);
        data.set(component.getKey(), PersistentDataType.TAG_CONTAINER, container);
        session.applyChanges();
    }

    public void remove(final AttributeId<?> attribute) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        data.remove(attribute.getKey());
        session.applyChanges();
    }

    public void remove(final ComponentId component, final AttributeId<?> attribute) {
        final StorageSession session = startSession();
        final PersistentDataContainer data = session.getData();
        final PersistentDataContainer container = data.get(component.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            throw new IllegalStateException();
        }
        container.remove(attribute.getKey());
        data.set(component.getKey(), PersistentDataType.TAG_CONTAINER, container);
        session.applyChanges();
    }
}
