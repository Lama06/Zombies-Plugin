package io.lama06.zombies;


import java.util.List;

public final class InvalidConfigException extends Exception {
    public static void mustBeSet(final Object o, final String name) throws InvalidConfigException {
        if (o == null || (o instanceof final String s && s.isEmpty())) {
            throw new InvalidConfigException(name + " not defined");
        }
    }

    public static void checkList(
            final List<? extends CheckableConfig> list,
            final boolean allowEmpty,
            final String name
    ) throws InvalidConfigException {
        mustBeSet(list, name);
        if (!allowEmpty && list.isEmpty()) {
            throw new InvalidConfigException("no entries");
        }
        for (int i = 0; i < list.size(); i++) {
            final CheckableConfig value = list.get(i);
            mustBeSet(value, "element " + (i + 1));
            try {
                value.check();
            } catch (final InvalidConfigException e) {
                throw new InvalidConfigException("error with element %d of %s".formatted(i + 1, name), e);
            }
        }
    }

    public InvalidConfigException(final String message) {
        super(message);
    }

    public InvalidConfigException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        final Throwable cause = getCause();
        if (cause == null) {
            return super.getMessage();
        }
        return super.getMessage() + ": " + cause.getMessage();
    }
}
