package io.lama06.zombies.event.weapon;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.HandlerList;

public final class WeaponDelayChangeEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final int oldDelay;
    private final int newDelay;

    public WeaponDelayChangeEvent(final Weapon weapon, final int oldDelay, final int newDelay) {
        super(weapon);
        this.oldDelay = oldDelay;
        this.newDelay = newDelay;
    }

    public int getOldDelay() {
        return oldDelay;
    }

    public int getNewDelay() {
        return newDelay;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
