package io.lama06.zombies.weapon.delay;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class WeaponDelayChangeEvent extends WeaponEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
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
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
