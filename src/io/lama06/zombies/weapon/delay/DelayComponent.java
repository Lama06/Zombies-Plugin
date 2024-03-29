package io.lama06.zombies.weapon.delay;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponent;
import org.bukkit.Bukkit;

public final class DelayComponent extends WeaponComponent {
    private final int delay;
    private int remainingDelay;

    public DelayComponent(final Weapon weapon, final int delay) {
        super(weapon);
        this.delay = delay;
    }

    public boolean isReady() {
        return remainingDelay == 0;
    }

    public void startDelay() {
        setRemainingDelay(delay);
    }

    public int getDelay() {
        return delay;
    }

    public int getRemainingDelay() {
        return remainingDelay;
    }

    public void setRemainingDelay(final int remainingDelay) {
        final int oldRemainingDelay = this.remainingDelay;
        this.remainingDelay = remainingDelay;
        Bukkit.getPluginManager().callEvent(new WeaponDelayChangeEvent(weapon, oldRemainingDelay, remainingDelay));
    }
}
