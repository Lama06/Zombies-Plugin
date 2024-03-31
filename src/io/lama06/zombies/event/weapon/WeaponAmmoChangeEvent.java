package io.lama06.zombies.event.weapon;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class WeaponAmmoChangeEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final int oldAmmo;
    private final int newAmmo;

    public WeaponAmmoChangeEvent(
            final Weapon weapon,
            final int oldAmmo,
            final int newAmmo
    ) {
        super(weapon);
        this.oldAmmo = oldAmmo;
        this.newAmmo = newAmmo;
    }

    public int getOldAmmo() {
        return oldAmmo;
    }

    public int getNewAmmo() {
        return newAmmo;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
