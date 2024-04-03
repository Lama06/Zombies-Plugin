package io.lama06.zombies.system.zombie.break_window;

import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.zombie.BreakWindowData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitWindowBreakingSystem implements Listener {
    @EventHandler
    private void onZombieSpawn(final ZombieSpawnEvent event) {
        final BreakWindowData data = event.getData().breakWindow;
        if (data == null) {
            return;
        }
        final Zombie zombie = event.getZombie();
        zombie.addComponent(ZombieComponents.BREAK_WINDOW);
    }
}
