package io.lama06.zombies.menu;

import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.util.BlockArea;
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

public final class BlockAreaSelection implements Listener {
    public static void open(
            final Player player,
            final Component title,
            final Runnable cancelCallback,
            final Consumer<? super BlockArea> callback
    ) {
        if (!player.isConnected()) {
            return;
        }

        new BlockAreaSelection(player, title, cancelCallback, callback).start();
    }

    private final Player player;
    private final Component title;
    private final Runnable cancelCallback;
    private final Consumer<? super BlockArea> callback;

    private BlockPosition position1;

    private BlockAreaSelection(
            final Player player,
            final Component title,
            final Runnable cancelCallback,
            final Consumer<? super BlockArea> callback
    ) {
        this.player = player;
        this.title = title;
        this.cancelCallback = cancelCallback;
        this.callback = callback;
    }

    private void start() {
        player.showTitle(Title.title(title, Component.text("Click on the first block")));
        Bukkit.getPluginManager().registerEvents(this, ZombiesPlugin.INSTANCE);
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
        if (position1 == null) {
            position1 = clicked.getLocation().toBlock();
            player.showTitle(Title.title(title, Component.text("Now click on the second block")));
            return;
        }
        final BlockPosition position2 = clicked.getLocation().toBlock();
        final BlockArea area = new BlockArea(position1, position2);
        callback.accept(area);
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
