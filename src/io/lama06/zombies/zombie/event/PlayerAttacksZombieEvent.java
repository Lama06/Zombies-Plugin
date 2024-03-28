package io.lama06.zombies.zombie.event;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerAttacksZombieEvent extends ZombieEvent implements Cancellable {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private boolean cancel;
    private final ZombiesPlayer player;
    private final Weapon weapon;

    public PlayerAttacksZombieEvent(
            final Zombie zombie,
            final ZombiesPlayer player,
            final Weapon weapon
    ) {
        super(zombie);
        this.player = player;
        this.weapon = weapon;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public ZombiesPlayer getPlayer() {
        return player;
    }
}
