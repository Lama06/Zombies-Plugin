package io.lama06.zombies.event;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerAttacksZombieEvent extends ZombiesEvent implements Cancellable {
    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final Weapon weapon;
    private final Entity zombie;
    private boolean cancel;
    private boolean fire;
    private double baseDamage;
    private double damageModifier = 1;

    public PlayerAttacksZombieEvent(final Weapon weapon, final Entity zombie) {
        super(zombie.getWorld());
        this.zombie = zombie;
        this.weapon = weapon;
    }

    public Player getPlayer() {
        return weapon.owner();
    }

    public Entity getZombie() {
        return zombie;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setBaseDamage(final double baseDamage) {
        this.baseDamage = baseDamage;
    }

    public void applyDamageModifier(final double damageModifier) {
        this.damageModifier *= damageModifier;
    }

    public double getDamage() {
        return baseDamage * damageModifier;
    }

    public boolean getFire() {
        return fire;
    }

    public void setFire(final boolean fire) {
        this.fire = fire;
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
