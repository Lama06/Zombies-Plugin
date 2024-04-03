package io.lama06.zombies.system.zombie;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.*;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.zombie.ZombieType;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public final class SpawnZombiesSystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getGameWorlds()) {
            final int round = world.get(ZombiesWorld.ROUND);
            final int nextZombieTime = world.get(ZombiesWorld.NEXT_ZOMBIE_TIME);
            if (nextZombieTime > 0) {
                world.set(ZombiesWorld.NEXT_ZOMBIE_TIME, nextZombieTime - 1);
                continue;
            }
            final SpawnRate spawnRate = SpawnRate.SPAWN_RATES.get(round - 1);
            final Location spawnPoint = getNextSpawnPoint(world);
            if (spawnPoint == null) {
                continue;
            }
            final ZombieType type = getNextZombie(world);
            if (type == null) {
                continue;
            }
            world.spawnZombie(spawnPoint, type);
            world.set(ZombiesWorld.NEXT_ZOMBIE_TIME, spawnRate.spawnDelay());
        }
    }

    private ZombieType getNextZombie(final ZombiesWorld world) {
        final int remainingZombies = world.get(ZombiesWorld.REMAINING_ZOMBIES);
        final int round = world.get(ZombiesWorld.ROUND);
        if (remainingZombies == 0) {
            return null;
        }
        final List<ZombieType> types = new ArrayList<>();
        SpawnRate.SPAWN_RATES.get(round - 1).zombies().forEach((type, count) -> {
            for (int i = 0; i < count; i++) {
                types.add(type);
            }
        });
        final RandomGenerator rnd = ThreadLocalRandom.current();
        final ZombieType nextZombieType = types.get(rnd.nextInt(types.size()));
        world.set(ZombiesWorld.REMAINING_ZOMBIES, remainingZombies - 1);
        return nextZombieType;
    }

    private List<Window> getAvailableWindows(final ZombiesWorld world) {
        final WorldConfig config = world.getConfig();
        if (config == null) {
            return null;
        }
        final List<String> areas = world.get(ZombiesWorld.REACHABLE_AREAS);
        return config.windows.stream().filter(window -> areas.contains(window.area)).toList();
    }

    private Window getNearestWindowToPlayer(final ZombiesWorld world, final ZombiesPlayer player) {
        final List<Window> windows = getAvailableWindows(world);
        if (windows == null || windows.isEmpty()) {
            return null;
        }
        return windows.stream().min(Comparator.comparingDouble(window -> {
            final Vector windowVector = window.spawnLocation.coordinates().toVector();
            final Vector playerVector = player.getBukkit().getLocation().toVector();
            return windowVector.distance(playerVector);
        })).orElseThrow();
    }

    private Location getNextSpawnPoint(final ZombiesWorld world) {
        final RandomGenerator rnd = ThreadLocalRandom.current();
        final List<ZombiesPlayer> players = world.getPlayers();
        if (players.isEmpty()) {
            return null;
        }
        final ZombiesPlayer player = players.get(rnd.nextInt(players.size()));
        final Window window = getNearestWindowToPlayer(world, player);
        if (window == null) {
            return null;
        }
        return window.spawnLocation.toBukkit(world.getBukkit());
    }
}
