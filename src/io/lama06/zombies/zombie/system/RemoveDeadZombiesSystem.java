package io.lama06.zombies.zombie.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.event.ZombieDeadEvent;
import io.lama06.zombies.zombie.event.ZombieHealthChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public final class RemoveDeadZombiesSystem extends System {
    public RemoveDeadZombiesSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void handleHealthChange(final ZombieHealthChangeEvent event) {
        final Zombie zombie = event.getZombie();
        if (!game.getZombies().containsValue(zombie)) {
            return;
        }
        if (zombie.getHealth().getHealth() <= 0) {
            Bukkit.getPluginManager().callEvent(new ZombieDeadEvent(zombie));
            game.getZombies().values().remove(zombie);
            zombie.getEntity().remove();
        }
    }
}
