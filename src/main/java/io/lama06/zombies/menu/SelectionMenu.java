package io.lama06.zombies.menu;

import io.lama06.zombies.ZombiesPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public final class SelectionMenu implements Listener {
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

    private static final int TOTAL_WIDTH = 9;
    private static final int TOTAL_HEIGHT = 6;
    private static final int TOTAL_ITEMS = TOTAL_WIDTH * TOTAL_HEIGHT;
    private static final int MARGIN_TOP_ROW = 0;
    private static final int MARGIN_BOTTOM_ROW = TOTAL_HEIGHT - 1;
    private static final int MARGIN_LEFT_COLUMN = 0;
    private static final int MARGIN_RIGHT_COLUMN = TOTAL_WIDTH - 1;
    private static final int CONTENT_WIDTH = TOTAL_WIDTH - 2;
    private static final int CONTENT_HEIGHT = TOTAL_HEIGHT - 2;
    private static final int CONTENT_ITEMS = CONTENT_WIDTH * CONTENT_HEIGHT;
    private static final String KEY_PREFIX = "selection_menu_";
    private static final String ENTRY_INDEX_KEY = KEY_PREFIX + "entry_index";
    private static final String PREVIOUS_PAGE_KEY = KEY_PREFIX + "previous_page";
    private static final String NEXT_PAGE_KEY = KEY_PREFIX + "next_page";
    private static final String SWITCHING_PAGE_KEY = KEY_PREFIX + "switching_page";

    private static int inventoryCoordinatesToSlot(final int x, final int y) {
        return y * TOTAL_WIDTH + x;
    }

    private static int contentIndexToSlot(final int contentIndex) {
        final int y = 1 + contentIndex / CONTENT_WIDTH;
        final int x = 1 + contentIndex % CONTENT_WIDTH;
        return inventoryCoordinatesToSlot(x, y);
    }

    private final Player player;
    private final Component title;
    private final Runnable cancelCallback;
    private final SelectionEntry[] entries;
    private final Inventory[] pages;
    private int currentPage;

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
        pages = createPages();
    }

    private void open() {
        Bukkit.getPluginManager().registerEvents(this, ZombiesPlugin.INSTANCE);
        player.openInventory(pages[0]);
    }

    private int getNumberOfPages() {
        return 1 + (entries.length - 1) / CONTENT_ITEMS;
    }

    private ItemStack createMarginItem() {
        final RandomGenerator rnd = ThreadLocalRandom.current();
        final ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta meta = glass.getItemMeta();
        if (rnd.nextInt(30) == 0) {
            meta.displayName(Component.text("I'm just a margin! Nothing to see here!").color(NamedTextColor.GRAY));
        } else {
            meta.displayName(Component.empty());
        }
        glass.setItemMeta(meta);
        return glass;
    }

    private void setupMargin(final Inventory inventory) {
        // Top
        for (int x = 0; x < TOTAL_WIDTH; x++) {
            inventory.setItem(inventoryCoordinatesToSlot(x, MARGIN_TOP_ROW), createMarginItem());
        }

        // Bottom
        for (int x = 0; x < TOTAL_WIDTH; x++) {
            inventory.setItem(inventoryCoordinatesToSlot(x, MARGIN_BOTTOM_ROW), createMarginItem());
        }

        // Left
        for (int y = 0; y < TOTAL_HEIGHT; y++) {
            inventory.setItem(inventoryCoordinatesToSlot(MARGIN_LEFT_COLUMN, y), createMarginItem());
        }

        // Right
        for (int y = 0; y < TOTAL_HEIGHT; y++) {
            inventory.setItem(inventoryCoordinatesToSlot(MARGIN_RIGHT_COLUMN, y), createMarginItem());
        }
    }

    private ItemStack createNavigationArrow(final String text, final String key) {
        final ItemStack arrow = new ItemStack(Material.ARROW);
        final ItemMeta meta = arrow.getItemMeta();
        meta.displayName(Component.text(text));
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        final NamespacedKey namespacedKey = new NamespacedKey(ZombiesPlugin.INSTANCE, key);
        pdc.set(namespacedKey, PersistentDataType.BOOLEAN, true);
        arrow.setItemMeta(meta);
        return arrow;
    }

    private void setupNavigation(final Inventory inventory, final int pageIndex) {
        final int lastPageIndex = getNumberOfPages() - 1;
        if (pageIndex != 0) {
            final ItemStack previousPageItem = createNavigationArrow("Previous Page", PREVIOUS_PAGE_KEY);
            inventory.setItem(inventoryCoordinatesToSlot(MARGIN_LEFT_COLUMN, MARGIN_BOTTOM_ROW), previousPageItem);
        }
        if (pageIndex != lastPageIndex) {
            final ItemStack nextPageItem = createNavigationArrow("Next Page", NEXT_PAGE_KEY);
            inventory.setItem(inventoryCoordinatesToSlot(MARGIN_RIGHT_COLUMN, MARGIN_BOTTOM_ROW), nextPageItem);
        }
    }

    private void fillContent(final Inventory inventory, final int pageIndex) {
        final int firstEntryIndex = pageIndex * CONTENT_ITEMS;
        final int lastEntryIndexExclusive = Math.min(entries.length, firstEntryIndex + CONTENT_ITEMS);
        for (int entryIndex = firstEntryIndex; entryIndex < lastEntryIndexExclusive; entryIndex++) {
            final SelectionEntry entry = entries[entryIndex];
            final int contentIndexOnPage = entryIndex % CONTENT_ITEMS;
            final int slot = contentIndexToSlot(contentIndexOnPage);
            final ItemStack item = new ItemStack(entry.item());
            final ItemMeta meta = item.getItemMeta();
            meta.displayName(entry.name());
            if (entry.secondAction() != null) {
                meta.lore(List.of(Component.text("Right click: ").append(entry.secondActionDescription())));
            }
            final NamespacedKey indexKey = new NamespacedKey(ZombiesPlugin.INSTANCE, ENTRY_INDEX_KEY);
            meta.getPersistentDataContainer().set(indexKey, PersistentDataType.INTEGER, entryIndex);
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
        }
    }

    private Inventory createPage(final int pageIndex) {
        final int numberOfPages = getNumberOfPages();
        final boolean singlePage = numberOfPages == 1;
        final Component pageTitle = singlePage
                ? title : title.append(Component.text(" (Page %d of %d)".formatted(pageIndex + 1, numberOfPages)));
        final Inventory inventory = Bukkit.createInventory(null, TOTAL_ITEMS, pageTitle);
        setupMargin(inventory);
        setupNavigation(inventory, pageIndex);
        fillContent(inventory, pageIndex);
        return inventory;
    }

    private Inventory[] createPages() {
        final int numberOfPages = getNumberOfPages();
        final Inventory[] pages = new Inventory[numberOfPages];
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++){
            pages[pageIndex] = createPage(pageIndex);
        }
        return pages;
    }

    private void switchPage(final int direction) {
        if (direction == -1 && currentPage == 0) {
            return;
        }
        if (direction == 1 && currentPage == pages.length - 1) {
            return;
        }
        currentPage += direction;
        Bukkit.getScheduler().runTask(ZombiesPlugin.INSTANCE, () -> {
            final PersistentDataContainer pdc = player.getPersistentDataContainer();
            final NamespacedKey switchingPageKey = new NamespacedKey(ZombiesPlugin.INSTANCE, SWITCHING_PAGE_KEY);
            pdc.set(switchingPageKey, PersistentDataType.BOOLEAN, true);
            player.closeInventory();
            pdc.remove(switchingPageKey);
            player.openInventory(pages[currentPage]);
        });
    }

    @EventHandler
    private void onInventoryClick(final InventoryClickEvent event) {
        if (!event.getWhoClicked().equals(player)) {
            return;
        }
        event.setCancelled(true);
        if (!event.getClick().isMouseClick()) {
            return;
        }
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(pages[currentPage])) {
            return;
        }
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }
        final PersistentDataContainer pdc = clickedItem.getItemMeta().getPersistentDataContainer();
        final NamespacedKey entryIndexKey = new NamespacedKey(ZombiesPlugin.INSTANCE, ENTRY_INDEX_KEY);
        final NamespacedKey previousPageKey = new NamespacedKey(ZombiesPlugin.INSTANCE, PREVIOUS_PAGE_KEY);
        final NamespacedKey nextPageKey = new NamespacedKey(ZombiesPlugin.INSTANCE, NEXT_PAGE_KEY);
        if (pdc.has(previousPageKey)) {
            switchPage(-1);
        } else if (pdc.has(nextPageKey)) {
            switchPage(1);
        } else if (pdc.has(entryIndexKey)) {
            final int entryIndex = Objects.requireNonNull(pdc.get(entryIndexKey, PersistentDataType.INTEGER));
            if (entryIndex < 0 || entryIndex >= entries.length) {
                return;
            }
            HandlerList.unregisterAll(this);
            Bukkit.getScheduler().runTask(ZombiesPlugin.INSTANCE, () -> {
                player.closeInventory();
                if (event.getClick().isLeftClick()) {
                    entries[entryIndex].callback().run();
                } else if (event.getClick().isRightClick()) {
                    entries[entryIndex].secondAction().run();
                }
            });
        }
    }

    @EventHandler
    private void onInventoryClose(final InventoryCloseEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        final PersistentDataContainer pdc = player.getPersistentDataContainer();
        final NamespacedKey switchingPageKey = new NamespacedKey(ZombiesPlugin.INSTANCE, SWITCHING_PAGE_KEY);
        if (pdc.getOrDefault(switchingPageKey, PersistentDataType.BOOLEAN, false)) {
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
