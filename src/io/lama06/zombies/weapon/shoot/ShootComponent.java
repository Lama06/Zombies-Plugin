package io.lama06.zombies.weapon.shoot;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponent;

public final class ShootComponent extends WeaponComponent {
    private final int bullets;
    private final double damage;
    private final double damageCrit;
    private final double precision;

    public ShootComponent(final Weapon weapon, final ShootData data) {
        super(weapon);
        bullets = data.bullets();
        damage = data.damage();
        damageCrit = data.damageCrit();
        precision = data.precision();
    }

    public int getBullets() {
        return bullets;
    }

    public double getDamage() {
        return damage;
    }

    public double getDamageCrit() {
        return damageCrit;
    }

    public double getPrecision() {
        return precision;
    }
}