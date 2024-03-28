package io.lama06.zombies.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EnumSelectionMenu {
    public static <T extends Enum<T> & MenuDisplayableEnum> void open(
            final Class<T> enumClass,
            final Player player,
            final Component title,
            final Runnable cancelCallback,
            final Consumer<? super T> callback
    ) {
        final List<SelectionEntry> entries = new ArrayList<>();
        for (final T constant : enumClass.getEnumConstants()) {
            entries.add(new SelectionEntry(
                    constant.getDisplayName(),
                    constant.getMaterial(),
                    () -> {
                        callback.accept(constant);
                    }
            ));
        }
        SelectionMenu.open(
                player,
                title,
                cancelCallback,
                entries.toArray(SelectionEntry[]::new)
        );
    }
}
