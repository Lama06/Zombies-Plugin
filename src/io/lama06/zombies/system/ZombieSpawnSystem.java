package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.*;
import io.lama06.zombies.event.GameStartEvent;
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

public final class ZombieSpawnSystem implements Listener {
    @EventHandler
    private void initZombieSpawning(final GameStartEvent event) {
        final ZombiesWorld world = event.getWorld();
        final SpawnRate firstRoundSpawnRate = SpawnRate.SPAWN_RATES.getFirst();
        world.set(WorldAttributes.NEXT_ZOMBIE_TIME, firstRoundSpawnRate.spawnDelay());
        world.set(WorldAttributes.REMAINING_ZOMBIES, firstRoundSpawnRate.getNumberOfZombies());
    }

    @EventHandler
    private void spawnZombies(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getWorlds()) {
            final int round = world.get(WorldAttributes.ROUND);
            final int nextZombieTime = world.get(WorldAttributes.NEXT_ZOMBIE_TIME);
            if (nextZombieTime > 0) {
                world.set(WorldAttributes.NEXT_ZOMBIE_TIME, nextZombieTime - 1);
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
            world.spawnZombie(spawnPoint, type.data);
            world.set(WorldAttributes.NEXT_ZOMBIE_TIME, spawnRate.spawnDelay());
        }
    }

    private ZombieType getNextZombie(final ZombiesWorld world) {
        final int remainingZombies = world.get(WorldAttributes.REMAINING_ZOMBIES);
        final int round = world.get(WorldAttributes.ROUND);
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
        world.set(WorldAttributes.REMAINING_ZOMBIES, remainingZombies - 1);
        return nextZombieType;
    }

    private List<Window> getAvailableWindows(final ZombiesWorld world) {
        final WorldConfig config = ZombiesPlugin.getConfig(world);
        if (config == null) {
            return null;
        }
        final List<String> areas = world.get(WorldAttributes.REACHABLE_AREAS);
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
