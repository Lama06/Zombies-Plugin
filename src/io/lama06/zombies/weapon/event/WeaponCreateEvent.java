package io.lama06.zombies.weapon.event;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.weapon.WeaponData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public final class WeaponCreateEvent extends Event {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final WeaponData data;
    private final ItemStack item;
    private final ItemMeta meta;

    public WeaponCreateEvent(final WeaponData data, final ItemStack item, final ItemMeta meta) {
        this.data = data;
        this.item = item;
        this.meta = meta;
    }

    public WeaponData getData() {
        return data;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public PersistentDataContainer getPdc() {
        return meta.getPersistentDataContainer();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
