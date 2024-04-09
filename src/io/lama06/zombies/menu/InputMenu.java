package io.lama06.zombies.menu;

import io.lama06.zombies.ZombiesPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public final class InputMenu<T> implements Listener {
    public static <T> void open(
            final Player player,
            final Component title,
            final T initialData,
            final InputType<T> inputType,
            final Consumer<? super T> callback,
            final Runnable cancelCallback
    ) {
        if (!player.isConnected()) {
            return;
        }

        new InputMenu<>(player, title, initialData, inputType, callback, cancelCallback).open();
    }

    private final Player player;
    private final Component title;
    private final T initialData;
    private final InputType<T> inputType;
    private final Consumer<? super T> callback;
    private final Runnable cancelCallback;

    private AnvilInventory inventory;

    private InputMenu(
            final Player player,
            final Component title,
            final T initialData,
            final InputType<T> inputType,
            final Consumer<? super T> callback,
            final Runnable cancelCallback
    ) {
        this.player = player;
        this.title = title;
        this.initialData = initialData;
        this.inputType = inputType;
        this.callback = callback;
        this.cancelCallback = cancelCallback != null ? cancelCallback : () -> {};
    }

    private void open() {
        final InventoryView view = player.openAnvil(null, true);
        if (view == null) {
            cancelCallback.run();
            return;
        }
        view.setTitle(LegacyComponentSerializer.legacySection().serialize(title));
        inventory = (AnvilInventory) view.getTopInventory();
        final ItemStack firstItem = new ItemStack(Material.PAPER);
        final ItemMeta firstItemMeta = firstItem.getItemMeta();
        firstItemMeta.displayName(Component.text(inputType.formatData(initialData)));
        firstItem.setItemMeta(firstItemMeta);
        inventory.setFirstItem(firstItem);
        Bukkit.getPluginManager().registerEvents(this, ZombiesPlugin.INSTANCE);
    }

    @EventHandler
    private void onInventoryClick(final InventoryClickEvent event) {
        if (!event.getWhoClicked().equals(player) || !event.getInventory().equals(inventory)) {
            return;
        }
        event.setCancelled(true);
        if (!event.getClick().isMouseClick() || event.getSlot() != 2) {
            return;
        }
        final T input;
        try {
            input = inputType.parseInput(inventory.getRenameText());
        } catch (final InvalidInputException e) {
            player.sendMessage(Component.text(e.getMessage()));
            return;
        }
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().runTask(ZombiesPlugin.INSTANCE, () -> {
            player.closeInventory();
            callback.accept(input);
        });
    }

    @EventHandler
    private void onInventoryClose(final InventoryCloseEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        Bukkit.getScheduler().runTask(ZombiesPlugin.INSTANCE, cancelCallback);
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
