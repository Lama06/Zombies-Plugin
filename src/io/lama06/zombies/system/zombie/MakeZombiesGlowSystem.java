package io.lama06.zombies.system.zombie;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class MakeZombiesGlowSystem implements Listener {
    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld gameWorld : ZombiesPlugin.INSTANCE.getGameWorlds()) {
            final int remainingZombies = gameWorld.get(ZombiesWorld.REMAINING_ZOMBIES);
            if (remainingZombies != 0) {
                continue;
            }
            for (final Zombie zombie : gameWorld.getZombies()) {
                final Entity entity = zombie.getEntity();
                if (entity.isGlowing()) {
                    continue;
                }
                entity.setGlowing(true);
            }
        }
    }
}
