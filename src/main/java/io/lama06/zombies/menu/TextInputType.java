package io.lama06.zombies.menu;

public final class TextInputType implements InputType<String> {
    @Override
    public String parseInput(final String input) {
        return input;
    }

    @Override
    public String formatData(final String data) {
        return data;
    }
}
