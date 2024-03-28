package io.lama06.zombies.menu;

public interface InputType<T> {
    T parseInput(final String input) throws InvalidInputException;

    String formatData(final T data);
}
