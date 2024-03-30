package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.*;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.zombie.ZombieType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public final class ZombieSpawnSystem implements Listener {
    @EventHandler
    private void initZombieSpawning(final GameStartEvent event) {
        final SpawnRate firstRoundSpawnRate = SpawnRate.SPAWN_RATES.getFirst();
        final PersistentDataContainer pdc = event.getWorld().getPersistentDataContainer();
        pdc.set(WorldAttributes.NEXT_ZOMBIE_TIME.getKey(), PersistentDataType.INTEGER, firstRoundSpawnRate.spawnDelay());
        pdc.set(WorldAttributes.REMAINING_ZOMBIES.getKey(), PersistentDataType.INTEGER, firstRoundSpawnRate.getNumberOfZombies());
    }

    @EventHandler
    private void spawnZombies(final ServerTickEndEvent event) {
        for (final World world : ZombiesWorld.getGameWorlds()) {
            final PersistentDataContainer pdc = world.getPersistentDataContainer();
            final Integer round = pdc.get(WorldAttributes.ROUND.getKey(), PersistentDataType.INTEGER);
            final Integer nextZombieTime = pdc.get(WorldAttributes.NEXT_ZOMBIE_TIME.getKey(), PersistentDataType.INTEGER);
            if (round == null || nextZombieTime == null) {
                continue;
            }
            if (nextZombieTime > 0) {
                pdc.set(WorldAttributes.NEXT_ZOMBIE_TIME.getKey(), PersistentDataType.INTEGER, nextZombieTime - 1);
                continue;
            }
            final SpawnRate spawnRate = SpawnRate.SPAWN_RATES.get(round - 1);
            final Location spawnPoint = getNextSpawnPoint(world);
            if (spawnPoint == null) {
                return;
            }
            final ZombieType type = getNextZombie(world);
            if (type == null) {
                return;
            }
            type.data.spawn(spawnPoint);
            pdc.set(WorldAttributes.NEXT_ZOMBIE_TIME.getKey(), PersistentDataType.INTEGER, spawnRate.spawnDelay());

        }
    }

    private ZombieType getNextZombie(final World world) {
        final PersistentDataContainer pdc = world.getPersistentDataContainer();
        final Integer remainingZombies = pdc.get(WorldAttributes.REMAINING_ZOMBIES.getKey(), PersistentDataType.INTEGER);
        final Integer round = pdc.get(WorldAttributes.ROUND.getKey(), PersistentDataType.INTEGER);
        if (remainingZombies == null || round == null || remainingZombies == 0) {
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
        pdc.set(WorldAttributes.REMAINING_ZOMBIES.getKey(), PersistentDataType.INTEGER, remainingZombies - 1);
        return nextZombieType;
    }

    private List<Window> getAvailableWindows(final World world) {
        final WorldConfig config = ZombiesPlugin.getConfig(world);
        if (config == null) {
            return null;
        }
        final PersistentDataContainer pdc = world.getPersistentDataContainer();
        final List<String> areas = pdc.get(WorldAttributes.REACHABLE_AREAS.getKey(), PersistentDataType.LIST.strings());
        if (areas == null) {
            return null;
        }
        return config.windows.stream().filter(window -> areas.contains(window.area)).toList();
    }

    private Window getNearestWindowToPlayer(final World world, final Player player) {
        final List<Window> windows = getAvailableWindows(world);
        if (windows == null || windows.isEmpty()) {
            return null;
        }
        return windows.stream().min(Comparator.comparingDouble(window -> {
            final Vector windowVector = window.spawnLocation.coordinates().toVector();
            final Vector playerVector = player.getLocation().toVector();
            return windowVector.distance(playerVector);
        })).orElseThrow();
    }

    private Location getNextSpawnPoint(final World world) {
        final RandomGenerator rnd = ThreadLocalRandom.current();
        final List<Player> players = world.getPlayers();
        if (players.isEmpty()) {
            return null;
        }
        final Player player = players.get(rnd.nextInt(players.size()));
        final Window window = getNearestWindowToPlayer(world, player);
        if (window == null) {
            return null;
        }
        return window.spawnLocation.toBukkit(world);
    }
}
