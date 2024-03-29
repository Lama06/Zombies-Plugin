package io.lama06.zombies.weapon.reload;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.weapon.shoot.WeaponShootEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public final class PreventShootDuringReloadSystem extends System {
    public PreventShootDuringReloadSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onShoot(final WeaponShootEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        if (event.getWeapon().getReload() == null) {
            return;
        }
        if (event.getWeapon().getReload().isReady()) {
            return;
        }
        event.setCancelled(true);
    }
}
