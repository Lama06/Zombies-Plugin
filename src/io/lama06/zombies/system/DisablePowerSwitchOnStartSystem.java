package io.lama06.zombies.system;

import io.lama06.zombies.WorldAttributes;
import io.lama06.zombies.WorldConfig;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class DisablePowerSwitchOnStartSystem implements Listener {
    @EventHandler
    private void onGameStart(final GameStartEvent event) {
        final ZombiesWorld world = event.getWorld();
        final WorldConfig config = world.getConfig();
        if (config.powerSwitch == null) {
            return;
        }
        config.powerSwitch.setActive(world.getBukkit(), false);
        world.set(WorldAttributes.POWER_SWITCH, false);
    }
}
