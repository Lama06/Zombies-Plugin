package io.lama06.zombies.weapon.component;

import io.lama06.zombies.weapon.Weapon;

public final class MeleeComponent extends WeaponComponent {
    private final double damage;
    private final double range;

    public MeleeComponent(final Weapon weapon, final MeleeData data) {
        super(weapon);
        damage = data.damage();
        range = data.range();
    }

    public double getDamage() {
        return damage;
    }

    public double getRange() {
        return range;
    }
}
