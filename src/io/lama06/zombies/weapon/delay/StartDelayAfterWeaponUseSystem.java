package io.lama06.zombies.weapon.delay;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.weapon.event.WeaponUseEvent;
import org.bukkit.event.EventHandler;

public final class StartDelayAfterWeaponUseSystem extends System {
    public StartDelayAfterWeaponUseSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler(ignoreCancelled = true)
    private void onWeaponUse(final WeaponUseEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        if (event.getWeapon().getDelay() == null) {
            return;
        }
        event.getWeapon().getDelay().startDelay();
    }
}
