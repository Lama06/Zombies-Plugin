package io.lama06.zombies.weapon.ammo;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class WeaponClipChangeEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final int oldClip;
    private final int newClip;

    public WeaponClipChangeEvent(
            final Weapon weapon,
            final int oldClip,
            final int newClip
    ) {
        super(weapon);
        this.oldClip = oldClip;
        this.newClip = newClip;
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
