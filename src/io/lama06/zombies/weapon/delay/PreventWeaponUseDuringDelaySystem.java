package io.lama06.zombies.weapon.delay;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.weapon.event.WeaponUseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public final class PreventWeaponUseDuringDelaySystem extends System {
    public PreventWeaponUseDuringDelaySystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onWeaponUse(final WeaponUseEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        if (event.getWeapon().getDelay() == null) {
            return;
        }
        if (event.getWeapon().getDelay().isReady()) {
            return;
        }
        event.setCancelled(true);
    }
}
