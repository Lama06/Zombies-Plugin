package io.lama06.zombies.weapon.component;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponAmmoChangeEvent;
import org.bukkit.Bukkit;

public final class AmmoComponent extends WeaponComponent {
    private final int maxAmmo;
    private int ammo;
    private final int maxClip;
    private int clip;

    public AmmoComponent(final Weapon weapon, final AmmoData data) {
        super(weapon);
        maxAmmo = data.ammo();
        maxClip = data.clip();
        ammo = maxAmmo;
        clip = maxClip;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(final int ammo) {
        final int oldAmmo = this.ammo;
        this.ammo = ammo;
        Bukkit.getPluginManager().callEvent(new WeaponAmmoChangeEvent(weapon, oldAmmo, ammo, clip, clip));
    }

    public int getMaxClip() {
        return maxClip;
    }

    public int getClip() {
        return clip;
    }

    public void setClip(final int clip) {
        final int oldClip = this.clip;
        this.clip = clip;
        Bukkit.getPluginManager().callEvent(new WeaponAmmoChangeEvent(weapon, ammo, ammo, oldClip, clip));
    }
}
