package io.lama06.zombies.event.weapon;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class WeaponClipChangeEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
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
