package io.lama06.zombies;

import io.lama06.zombies.zombie.ZombieType;

import java.util.List;
import java.util.Map;

public record SpawnRate(int spawnDelay, Map<ZombieType, Integer> zombies, ZombieType boss) {
    public static final List<SpawnRate> SPAWN_RATES = List.of(
            new SpawnRate(5 * 20, Map.of(
                    ZombieType.GUARDIAN_ZOMBIE, 2
            ), ZombieType.BOMBIE)
    );

    public SpawnRate(final int spawnDelay, final Map<ZombieType, Integer> zombies) {
        this(spawnDelay, zombies, null);
    }

    public int getNumberOfZombies() {
        return zombies.values().stream().mapToInt(i -> i).sum();
    }
}