package io.lama06.zombies.event.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerAttackZombieEvent extends PlayerEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final Weapon weapon;
    private final Zombie zombie;
    private boolean fire;
    private boolean kill;
    private boolean freeze;
    private double baseDamage;
    private double damageModifier = 1;

    public PlayerAttackZombieEvent(final Weapon weapon, final Zombie zombie) {
        super(weapon.getPlayer());
        this.zombie = zombie;
        this.weapon = weapon;
    }

    public ZombiesPlayer getPlayer() {
        return weapon.getPlayer();
    }

    public Zombie getZombie() {
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

    public boolean isFire() {
        return fire;
    }

    public void setFire(final boolean fire) {
        this.fire = fire;
    }

    public boolean isKill() {
        return kill;
    }

    public void setKill(final boolean kill) {
        this.kill = kill;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(final boolean freeze) {
        this.freeze = freeze;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
