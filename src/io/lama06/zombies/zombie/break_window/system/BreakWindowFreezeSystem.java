package io.lama06.zombies.zombie.break_window.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.Zombie;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public final class BreakWindowFreezeSystem extends System {
    public BreakWindowFreezeSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onMove(final EntityMoveEvent event) {
        if (!game.getZombies().containsKey(event.getEntity())) {
            return;
        }
        final Zombie zombie = game.getZombies().get(event.getEntity());
        if (zombie.getBreakWindow() == null || !zombie.getBreakWindow().isBreakingWindow()) {
            return;
        }
        event.setCancelled(true);
    }
}
