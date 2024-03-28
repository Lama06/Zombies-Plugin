package io.lama06.zombies;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class InvalidConfigException extends Exception {
    @Contract(value = "null, _ -> fail", pure = true)
    public static void mustBeSet(final @Nullable Object o, final String name) throws InvalidConfigException {
        if (o == null || (o instanceof final String s && s.isEmpty())) {
            throw new InvalidConfigException(name + " not defined");
        }
    }

    public InvalidConfigException(final String message) {
        super(message);
    }

    public InvalidConfigException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
