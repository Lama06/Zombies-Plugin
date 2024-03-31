package io.lama06.zombies.weapon.shoot;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponUseEvent;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;

public final class WeaponShootEvent extends WeaponUseEvent implements Cancellable {
    private final List<Bullet> bullets;

    public WeaponShootEvent(final Weapon weapon, final List<Bullet> bullets) {
        super(weapon);
        this.bullets = bullets;
    }

    public @UnmodifiableView List<Bullet> getBullets() {
        return Collections.unmodifiableList(bullets);
    }
}
