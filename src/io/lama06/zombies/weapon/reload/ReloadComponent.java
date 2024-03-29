package io.lama06.zombies.weapon.reload;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponent;
import org.bukkit.Bukkit;

public final class ReloadComponent extends WeaponComponent {
    private final int reload;
    private int remainingReload;

    public ReloadComponent(final Weapon weapon, final int reload) {
        super(weapon);
        this.reload = reload;
    }

    public boolean isReady() {
        return remainingReload == 0;
    }

    public void startReload() {
        setRemainingReload(reload);
    }

    public int getReload() {
        return reload;
    }

    public int getRemainingReload() {
        return remainingReload;
    }

    public void setRemainingReload(final int remainingReload) {
        if (remainingReload < 0) {
            throw new IllegalArgumentException();
        }
        final int oldRemainingReload = this.remainingReload;
        this.remainingReload = remainingReload;
        Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(weapon, oldRemainingReload, remainingReload));
    }
}
