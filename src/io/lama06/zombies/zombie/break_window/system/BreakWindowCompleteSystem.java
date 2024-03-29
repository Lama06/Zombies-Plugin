package io.lama06.zombies.zombie.break_window.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.break_window.event.BreakWindowCompleteEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public final class BreakWindowCompleteSystem extends System {
    public BreakWindowCompleteSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onComplete(final BreakWindowCompleteEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        event.getZombie().getBreakWindow().getWindowBlock().toLocation(game.getWorld()).getBlock().setType(Material.AIR);
    }
}
