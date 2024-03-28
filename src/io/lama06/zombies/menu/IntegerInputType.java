package io.lama06.zombies.menu;

import org.jetbrains.annotations.NotNull;

public final class IntegerInputType implements InputType<Integer> {
    private final int from;
    private final int to;

    public IntegerInputType(final Integer from, final Integer to) {
        this.from = from == null ? Integer.MIN_VALUE : from;
        this.to = to == null ? Integer.MAX_VALUE : to;
    }

    public IntegerInputType() {
        this(0, Integer.MAX_VALUE);
    }

    @Override
    public Integer parseInput(final @NotNull String input) throws InvalidInputException {
        final int inputInteger;
        try {
            inputInteger = Integer.parseInt(input.trim());
        } catch (final NumberFormatException e) {
            throw new InvalidInputException("integer incorrectly formatted", e);
        }
        if (inputInteger < 0 && from >= 0) {
            throw new InvalidInputException("negative integers not allowed");
        }
        if (inputInteger < from || inputInteger > to) {
            throw new InvalidInputException("integer out of range [%s, %s]".formatted(from, to));
        }
        return inputInteger;
    }

    @Override
    public String formatData(final Integer data) {
        return data.toString();
    }
}
