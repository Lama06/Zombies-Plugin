package io.lama06.zombies.system.zombie;

import io.lama06.zombies.SpawnRate;
import io.lama06.zombies.WorldAttributes;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitZombieSpawningSystem implements Listener {
    @EventHandler
    private void onGameStart(final GameStartEvent event) {
        final ZombiesWorld world = event.getWorld();
        final SpawnRate firstRoundSpawnRate = SpawnRate.SPAWN_RATES.getFirst();
        world.set(WorldAttributes.NEXT_ZOMBIE_TIME, firstRoundSpawnRate.spawnDelay());
        world.set(WorldAttributes.REMAINING_ZOMBIES, firstRoundSpawnRate.getNumberOfZombies());
    }
}
