package io.lama06.zombies.weapon.event;

import io.lama06.zombies.event.ZombiesEvent;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.entity.Player;

public abstract class WeaponEvent extends ZombiesEvent {
    private final Weapon weapon;

    protected WeaponEvent(final Weapon weapon) {
        super(weapon.owner().getWorld());
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Player getPlayer() {
        return weapon.owner();
    }
}
