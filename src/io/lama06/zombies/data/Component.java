package io.lama06.zombies.data;

public interface Component {
    <T>T getOrDefault(final AttributeId<T> attribute, final T fallback);

    <T> T get(final AttributeId<T> attribute);

    <T> void set(final AttributeId<T> attribute, final T data);

    void remove(final AttributeId<?> attribute);
}
