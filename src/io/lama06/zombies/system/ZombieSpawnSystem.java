package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.System;
import io.lama06.zombies.Window;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieType;
import io.lama06.zombies.zombie.event.ZombieSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ZombieSpawnSystem extends System {
    public ZombieSpawnSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        if (game.getNextZombieTicks() > 0) {
            game.setNextZombieTicks(game.getNextZombieTicks() - 1);
        }
        if (game.getNextZombieTicks() != 0) {
            return;
        }
        game.setNextZombieTicks(10*20);
        final ZombieType type = ZombieType.NORMAL;
        final Location spawnPoint = getSpawnPoint();
        final Entity entity = world.spawnEntity(spawnPoint, type.data.entity());
        final Zombie zombie = new Zombie(game, entity, type.data);
        game.getZombies().put(entity, zombie);
        Bukkit.getPluginManager().callEvent(new ZombieSpawnEvent(zombie));
    }

    private List<Window> getAvailableWindows() {
        return game.getConfig().windows.stream()
                .filter(window -> game.getReachableAreas().contains(window.area))
                .toList();
    }

    private Location getSpawnPoint() {
        final List<Window> windows = getAvailableWindows();
        final Window window = windows.get(ThreadLocalRandom.current().nextInt(windows.size()));
        return window.spawnLocation.toBukkit(game.getWorld());
    }
}
