package io.lama06.zombies.system;

import io.lama06.zombies.*;
import io.lama06.zombies.event.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class CloseDoorsOnStartSystem implements Listener {
    @EventHandler
    private void onGameStart(final GameStartEvent event) {
        final ZombiesWorld world = event.getWorld();
        final WorldConfig config = ZombiesPlugin.getConfig(world);
        for (final Door door : config.doors) {
            door.setOpen(world, false);
        }
        world.set(WorldAttributes.OPEN_DOORS, List.of());
        world.set(WorldAttributes.REACHABLE_AREAS, List.of(config.startArea));
    }
}
