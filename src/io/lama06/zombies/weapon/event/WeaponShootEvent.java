package io.lama06.zombies.weapon.event;

import io.lama06.zombies.util.EventHandlerAccess;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class WeaponShootEvent extends WeaponEvent {
    public record Bullet(Vector direction) { }

    public static final HandlerList HANDLERS = new HandlerList();

    @EventHandlerAccess
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final List<Bullet> bullets;

    public WeaponShootEvent(final Weapon weapon, final List<Bullet> bullets) {
        super(weapon);
        this.bullets = bullets;
    }

    public List<Bullet> getBullets() {
        return Collections.unmodifiableList(bullets);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
