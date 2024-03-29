package io.lama06.zombies.weapon.melee;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponUseEvent;

public final class WeaponMeleeEvent extends WeaponUseEvent {
    public WeaponMeleeEvent(final Weapon weapon) {
        super(weapon);
    }
}
