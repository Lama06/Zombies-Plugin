package io.lama06.zombies.weapon;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.event.ZombiesEvent;

import java.util.Objects;

public abstract class WeaponEvent extends ZombiesEvent {
    private final Weapon weapon;

    protected WeaponEvent(final Weapon weapon) {
        super(weapon.getPlayer().getGame());
        this.weapon = Objects.requireNonNull(weapon);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public ZombiesPlayer getPlayer() {
        return weapon.getPlayer();
    }
}
