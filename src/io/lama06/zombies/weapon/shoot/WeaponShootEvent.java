package io.lama06.zombies.weapon.shoot;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponUseEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public final class WeaponShootEvent extends WeaponUseEvent implements Cancellable {
    public record Bullet(Vector direction) { }

    private final List<Bullet> bullets;

    public WeaponShootEvent(final Weapon weapon, final List<Bullet> bullets) {
        super(weapon);
        this.bullets = bullets;
    }

    public List<Bullet> getBullets() {
        return Collections.unmodifiableList(bullets);
    }
}
