package io.lama06.zombies.event.weapon;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponData;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class WeaponCreateEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final WeaponData data;

    public WeaponCreateEvent(final Weapon weapon, final WeaponData data) {
        super(weapon);
        this.data = data;
    }

    public WeaponData getData() {
        return data;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
