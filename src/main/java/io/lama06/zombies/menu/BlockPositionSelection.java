package io.lama06.zombies.menu;

import io.lama06.zombies.PlayerCancellation;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.event.player.PlayerCancelCommandEvent;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.function.Consumer;

public final class BlockPositionSelection implements Listener {
    public static void open(
            final Player player,
            final Component title,
            final Runnable cancelCallback,
            final Consumer<? super BlockPosition> callback
    ) {
        if (!player.isConnected()) {
            return;
        }

        new BlockPositionSelection(player, title, cancelCallback, callback).start();
    }

    private final Player player;
    private final Component title;
    private final Runnable cancelCallback;
    private final Consumer<? super BlockPosition> callback;

    private BlockPositionSelection(
            final Player player,
            final Component title,
            final Runnable cancelCallback,
            final Consumer<? super BlockPosition> callback
    ) {
        this.player = player;
        this.title = title;
        this.cancelCallback = cancelCallback;
        this.callback = callback;
    }

    private void start() {
        player.showTitle(Title.title(title, Component.text("Click on a block")));
        player.sendMessage(PlayerCancellation.CANCEL_MESSAGE);
        Bukkit.getPluginManager().registerEvents(this, ZombiesPlugin.INSTANCE);
    }

    private void cleanup() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        event.setCancelled(true);
        final Block clicked = event.getClickedBlock();
        if (clicked == null) {
            return;
        }
        callback.accept(clicked.getLocation().toBlock());
        cleanup();
    }

    @EventHandler
    private void onPlayerCancel(final PlayerCancelCommandEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        cancelCallback.run();
        cleanup();
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        cancelCallback.run();
        cleanup();
    }
}
