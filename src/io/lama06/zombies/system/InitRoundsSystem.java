package io.lama06.zombies.system;

import io.lama06.zombies.WorldAttributes;
import io.lama06.zombies.event.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitRoundsSystem implements Listener {
    @EventHandler
    private void onGameStart(final GameStartEvent event) {
        event.getWorld().set(WorldAttributes.ROUND, 1);
    }
}
