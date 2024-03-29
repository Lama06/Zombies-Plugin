package io.lama06.zombies.event;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerAttacksZombieEvent extends ZombiesEvent implements Cancellable {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final ZombiesPlayer player;
    private final Zombie zombie;
    private final Weapon weapon;
    private boolean cancel;
    private double damage;

    public PlayerAttacksZombieEvent(
            final ZombiesPlayer player,
            final Zombie zombie,
            final Weapon weapon,
            final double damage
    ) {
        super(player.getGame());
        this.player = player;
        this.zombie = zombie;
        this.weapon = weapon;
        this.damage = damage;
    }

    public ZombiesPlayer getPlayer() {
        return player;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(final double damage) {
        this.damage = damage;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
