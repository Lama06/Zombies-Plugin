package io.lama06.zombies.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ListConfigMenu {
    public static <T> void open(
            final Player player,
            final Component title,
            final List<T> list,
            final Material material,
            final Function<? super T, ? extends Component> stringer,
            final Supplier<? extends T> constructor,
            final Consumer<? super T> callback,
            final Runnable cancelCallback
    ) {
        if (!player.isConnected()) {
            return;
        }

        final Runnable reopen = () -> open(
                player,
                title,
                list,
                material,
                stringer,
                constructor,
                callback,
                cancelCallback
        );

        final List<SelectionEntry> entries = new ArrayList<>();
        entries.add(new SelectionEntry(
                Component.text("Add").color(NamedTextColor.GREEN),
                Material.GREEN_STAINED_GLASS_PANE,
                () -> {
                    list.add(constructor.get());
                    reopen.run();
                }
        ));
        for (int i = 0; i < list.size(); i++) {
            final int iCopy = i;
            final T object = list.get(i);
            entries.add(new SelectionEntry(
                    stringer.apply(object),
                    material,
                    () -> callback.accept(object),
                    Component.text("Delete").color(NamedTextColor.RED),
                    () -> {
                        list.remove(iCopy);
                        reopen.run();
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
