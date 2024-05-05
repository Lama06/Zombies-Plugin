package io.lama06.zombies.menu;

public final class InvalidInputException extends Exception {
    public InvalidInputException(final String message) {
        super(message);
    }

    public InvalidInputException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
