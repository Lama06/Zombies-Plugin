package io.lama06.zombies.weapon.event;

import io.lama06.zombies.event.ZombiesEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;

public abstract class WeaponEvent extends ZombiesEvent {
    private final Weapon weapon;

    protected WeaponEvent(final Weapon weapon) {
        super(weapon.getWorld());
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public ZombiesPlayer getPlayer() {
        return weapon.getPlayer();
    }
}
