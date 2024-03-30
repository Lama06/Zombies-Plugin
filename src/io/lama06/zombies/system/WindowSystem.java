package io.lama06.zombies.system;

import io.lama06.zombies.Window;
import io.lama06.zombies.WorldConfig;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.event.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class WindowSystem implements Listener {
    @EventHandler
    private void closeWindowsOnStart(final GameStartEvent event) {
        final WorldConfig config = ZombiesPlugin.getConfig(event.getWorld());
        for (final Window window : config.windows) {
            window.close(event.getWorld());
        }
    }
}
