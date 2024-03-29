package io.lama06.zombies.zombie.break_window.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.EventHandler;

public final class BreakWindowTickSystem extends System {
    public BreakWindowTickSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final Zombie zombie : game.getZombies().values()) {
            if (zombie.getBreakWindow() == null || !zombie.getBreakWindow().isBreakingWindow()) {
                continue;
            }
            zombie.getBreakWindow().tick();
        }
    }
}
