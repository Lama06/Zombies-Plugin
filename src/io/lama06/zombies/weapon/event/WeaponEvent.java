package io.lama06.zombies.weapon.event;

import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.Event;

import java.util.Objects;

public abstract class WeaponEvent extends Event {
    private final Weapon weapon;

    protected WeaponEvent(final Weapon weapon) {
        this.weapon = Objects.requireNonNull(weapon);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public ZombiesPlayer getPlayer() {
        return weapon.getPlayer();
    }

    public ZombiesGame getGame() {
        return getPlayer().getGame();
    }
}
