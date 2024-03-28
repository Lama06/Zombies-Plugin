package io.lama06.zombies.menu;

import io.lama06.zombies.ZombiesPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SelectionMenu implements Listener {
    private static final String ENTRY_INDEX_KEY = "selection_menu_entry_index";

    public static void open(
            final Player player,
            final Component title,
            final Runnable cancelCallback,
            final SelectionEntry... entries
    ) {
        if (!player.isConnected()) {
            return;
        }

        new SelectionMenu(player, title, cancelCallback, entries).open();
    }

    private final Player player;
    private final Component title;
    private final Runnable cancelCallback;
    private final SelectionEntry[] entries;
    private Inventory inventory;

    private SelectionMenu(
            final Player player,
            final Component title,
            final Runnable cancelCallback,
            final SelectionEntry... entries
    ) {
        this.player = player;
        this.title = title;
        this.cancelCallback = cancelCallback;
        this.entries = entries;
    }

    private void open() {
        Bukkit.getPluginManager().registerEvents(this, ZombiesPlugin.INSTANCE);
        inventory = createInventory();
        player.openInventory(inventory);
    }

    private @NotNull Inventory createInventory() {
        final Inventory inventory = Bukkit.createInventory(null, 9 * 6, title);

        final ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.displayName(Component.empty());
        glass.setItemMeta(glassMeta);

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y <= 5; y += 5) {
                inventory.setItem(y*9 + x, glass);
            }
        }

        for (int y = 1; y < 5; y++) {
            for (int x = 0; x <= 8; x += 8) {
                inventory.setItem(y*9 + x, glass);
            }
        }

        for (int i = 0; i < entries.length; i++) {
            final int row = i / 7 + 1;
            final int column = i % 7 + 1;
            final int slot = row*9 + column;
            final SelectionEntry entry = entries[i];
            final ItemStack item = new ItemStack(entry.item());
            final ItemMeta meta = item.getItemMeta();
            meta.displayName(entry.name());
            if (entry.secondAction() != null) {
                meta.lore(List.of(Component.text("Right click: ").append(entry.secondActionDescription())));
            }
            final NamespacedKey key = new NamespacedKey(ZombiesPlugin.INSTANCE, ENTRY_INDEX_KEY);
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, i);
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
        }

        return inventory;
    }

    @EventHandler
    private void onInventoryClick(final @NotNull InventoryClickEvent event) {
        if (event.getView().getTopInventory().equals(inventory) || event.getView().getBottomInventory().equals(inventory)) {
            event.setCancelled(true);
        }
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(inventory)) {
            return;
        }
        if (!event.getClick().isMouseClick()) {
            return;
        }
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }
        final NamespacedKey key = new NamespacedKey(ZombiesPlugin.INSTANCE, ENTRY_INDEX_KEY);
        final Integer i = clickedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        if (i == null || i < 0 || i >= entries.length) {
            return;
        }
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().runTask(ZombiesPlugin.INSTANCE, () -> {
            player.closeInventory();
            if (event.getClick().isLeftClick()) {
                entries[i].callback().run();
            } else if (event.getClick().isRightClick()) {
                entries[i].secondAction().run();
            }
        });
    }

    @EventHandler
    private void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }

        Bukkit.getScheduler().runTask(ZombiesPlugin.INSTANCE, cancelCallback);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    private void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }

        cancelCallback.run();
        HandlerList.unregisterAll(this);
    }
}
