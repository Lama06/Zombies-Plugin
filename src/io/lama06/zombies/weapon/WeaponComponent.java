package io.lama06.zombies.weapon;

public abstract class WeaponComponent {
    protected final Weapon weapon;

    public WeaponComponent(final Weapon weapon) {
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
