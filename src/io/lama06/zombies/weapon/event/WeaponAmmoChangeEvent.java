package io.lama06.zombies.weapon.event;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class WeaponAmmoChangeEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final int oldAmmo;
    private final int newAmmo;
    private final int oldClip;
    private final int newClip;

    public WeaponAmmoChangeEvent(
            final Weapon weapon,
            final int oldAmmo,
            final int newAmmo,
            final int oldClip,
            final int newClip
    ) {
        super(weapon);
        this.oldAmmo = oldAmmo;
        this.newAmmo = newAmmo;
        this.oldClip = oldClip;
        this.newClip = newClip;
    }

    public int getOldAmmo() {
        return oldAmmo;
    }

    public int getNewAmmo() {
        return newAmmo;
    }

    public int getOldClip() {
        return oldClip;
    }

    public int getNewClip() {
        return newClip;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
