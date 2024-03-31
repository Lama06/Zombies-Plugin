package io.lama06.zombies.event.weapon;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class WeaponReloadChangeEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final int oldReload;
    private final int newReload;

    public WeaponReloadChangeEvent(final Weapon weapon, final int oldReload, final int newReload) {
        super(weapon);
        this.oldReload = oldReload;
        this.newReload = newReload;
    }

    public int getOldReload() {
        return oldReload;
    }

    public int getNewReload() {
        return newReload;
    }

    public boolean isComplete() {
        return newReload == 0;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
