package io.lama06.zombies.menu;

import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.util.EntityPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.function.Consumer;

public final class EntityPositionSelection implements Listener {
    public static void open(
            final Player player,
            final Component title,
            final Runnable cancelCallback,
            final Consumer<? super EntityPosition> callback
    ) {
        if (!player.isConnected()) {
            return;
        }

        new EntityPositionSelection(player, title, cancelCallback, callback).start();
    }

    private final Player player;
    private final Component title;
    private final Runnable cancelCallback;
    private final Consumer<? super EntityPosition> callback;

    private EntityPositionSelection(
            final Player player,
            final Component title,
            final Runnable cancelCallback,
            final Consumer<? super EntityPosition> callback
    ) {
        this.player = player;
        this.title = title;
        this.cancelCallback = cancelCallback;
        this.callback = callback;
    }

    private void start() {
        player.showTitle(Title.title(title, Component.text("Move to a new position and press ").append(Component.keybind("key.drop"))));
        Bukkit.getPluginManager().registerEvents(this, ZombiesPlugin.INSTANCE);
    }

    @EventHandler
    private void onPlayerDropItem(final PlayerDropItemEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        callback.accept(EntityPosition.ofEntity(event.getPlayer()));
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        cancelCallback.run();
        HandlerList.unregisterAll(this);
    }
}
