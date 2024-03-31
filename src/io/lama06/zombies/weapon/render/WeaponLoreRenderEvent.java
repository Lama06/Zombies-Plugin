package io.lama06.zombies.weapon.render;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WeaponLoreRenderEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    private final Map<LorePart, List<LoreEntry>> lore = new HashMap<>();

    public WeaponLoreRenderEvent(final Weapon weapon) {
        super(weapon);
    }

    public void addLore(final LorePart part, final List<LoreEntry> lore) {
        this.lore.put(part, lore);
    }

    public Map<LorePart, List<LoreEntry>> getLore() {
        return lore;
    }

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}