package io.lama06.zombies.event.weapon;

import io.lama06.zombies.weapon.Weapon;

public final class WeaponMeleeEvent extends WeaponUseEvent {
    public WeaponMeleeEvent(final Weapon weapon) {
        super(weapon);
    }
}
