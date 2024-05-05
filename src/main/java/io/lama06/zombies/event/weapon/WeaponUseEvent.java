package io.lama06.zombies.event.weapon;

import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public abstract class WeaponUseEvent extends WeaponEvent implements Cancellable {
    public static final HandlerList HANDLERS = new HandlerList();

    private boolean cancel;

    protected WeaponUseEvent(final Weapon weapon) {
        super(weapon);
    }

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
