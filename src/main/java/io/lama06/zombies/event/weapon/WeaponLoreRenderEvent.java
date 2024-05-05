package io.lama06.zombies.event.weapon;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.weapon.Weapon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WeaponLoreRenderEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    private final Map<Part, List<Entry>> lore = new HashMap<>();

    public WeaponLoreRenderEvent(final Weapon weapon) {
        super(weapon);
    }

    public void addLore(final Part part, final List<Entry> lore) {
        this.lore.put(part, lore);
    }

    public Map<Part, List<Entry>> getLore() {
        return lore;
    }

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public record Entry(String name, String value) {
        public Component toComponent() {
            return Component.text(name)
                    .append(Component.text(": "))
                    .append(Component.text(value).color(NamedTextColor.GREEN));
        }
    }

    public enum Part {
        ATTACK,
        SHOOT,
        MELEE,
        AMMO,
        DELAY,
        RELOAD
    }
}
