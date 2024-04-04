package io.lama06.zombies;

import io.lama06.zombies.zombie.ZombieType;

import java.util.List;
import java.util.Map;

public record SpawnRate(int spawnDelay, Map<ZombieType, Integer> zombies, ZombieType boss) {
    public static final List<SpawnRate> SPAWN_RATES = List.of(
            // 1:
            new SpawnRate(5 * 20, Map.of(
                    ZombieType.NORMAL_EASY, 10
            )),
            // 2:
            new SpawnRate(4 * 20 + 10, Map.of(
                    ZombieType.NORMAL_EASY, 7,
                    ZombieType.NORMAL_MEDIUM, 5
            )),
            // 3:
            new SpawnRate(4 * 20, Map.of(
                    ZombieType.NORMAL_EASY, 5,
                    ZombieType.NORMAL_MEDIUM, 7,
                    ZombieType.NORMAL_HARD, 2
            )),
            // 4:
            new SpawnRate(3 * 20 + 10, Map.of(
                    ZombieType.NORMAL_EASY, 3,
                    ZombieType.NORMAL_MEDIUM, 7,
                    ZombieType.NORMAL_HARD, 6
            )),
            // 5:
            new SpawnRate(3 * 20, Map.of(
                    ZombieType.NORMAL_MEDIUM, 10,
                    ZombieType.LITTLE_BOMBIE, 8
            )),
            // 6:
            new SpawnRate(3 * 20, Map.of(
                    ZombieType.ZOMBIE_WOLF, 8,
                    ZombieType.NORMAL_HARD, 8,
                    ZombieType.PIG_ZOMBIE, 2
            )),
            // 7:
            new SpawnRate(2 * 20 + 10, Map.of(
                    ZombieType.NORMAL_HARD, 7,
                    ZombieType.PIG_ZOMBIE, 8,
                    ZombieType.GUARDIAN_ZOMBIE, 2
            )),
            // 8:
            new SpawnRate(2 * 20 + 10, Map.of(
                    ZombieType.NORMAL_HARD, 7,
                    ZombieType.ZOMBIE_WOLF, 8,
                    ZombieType.NORMAL_MEDIUM, 3
            )),
            // 9:
            new SpawnRate(2 * 20, Map.of(
                    ZombieType.NORMAL_HARD, 7,
                    ZombieType.ZOMBIE_WOLF, 13
            )),
            // 10:
            new SpawnRate(2 * 20, Map.of(
                    ZombieType.NORMAL_HARD, 10,
                    ZombieType.ZOMBIE_WOLF, 5,
                    ZombieType.PIG_ZOMBIE, 6,
                    ZombieType.LITTLE_BOMBIE, 4
            ), ZombieType.BOMBIE),
            // 11:
            new SpawnRate(2 * 20, Map.of(
                    ZombieType.NORMAL_HARD, 14,
                    ZombieType.ZOMBIE_WOLF, 4,
                    ZombieType.PIG_ZOMBIE, 5,
                    ZombieType.LITTLE_BOMBIE, 3
            )),
            // 12:
            new SpawnRate(2 * 20, Map.of(
                    ZombieType.MAGMA_ZOMBIE, 15,
                    ZombieType.NORMAL_HARD, 15
            )),
            // 13
            new SpawnRate(30, Map.of(
                    ZombieType.MAGMA_ZOMBIE, 15,
                    ZombieType.NORMAL_HARD, 15,
                    ZombieType.FIRE_ZOMBIE, 2
            )),
            // 14:
            new SpawnRate(30, Map.of(
                    ZombieType.MAGMA_ZOMBIE, 15,
                    ZombieType.NORMAL_HARD, 15,
                    ZombieType.FIRE_ZOMBIE, 3,
                    ZombieType.GUARDIAN_ZOMBIE, 3
            )),
            // 15:
            new SpawnRate(30, Map.of(
                    ZombieType.MAGMA_ZOMBIE, 15,
                    ZombieType.NORMAL_HARD, 15,
                    ZombieType.LITTLE_BOMBIE, 8
            )),
            // 16:
            new SpawnRate(25, Map.of(
                    ZombieType.GUARDIAN_ZOMBIE, 4,
                    ZombieType.FIRE_ZOMBIE, 4,
                    ZombieType.NORMAL_HARD, 15,
                    ZombieType.LITTLE_BOMBIE, 8,
                    ZombieType.ZOMBIE_WOLF, 8,
                    ZombieType.MAGMA_ZOMBIE, 4
            )),
            // 17:
            new SpawnRate(25, Map.of(
                    ZombieType.GUARDIAN_ZOMBIE, 10,
                    ZombieType.FIRE_ZOMBIE, 10,
                    ZombieType.ZOMBIE_WOLF, 10,
                    ZombieType.NORMAL_HARD, 10,
                    ZombieType.PIG_ZOMBIE, 10
            )),
            // 18
            new SpawnRate(25, Map.of(
                    ZombieType.GUARDIAN_ZOMBIE, 20,
                    ZombieType.FIRE_ZOMBIE, 15,
                    ZombieType.ZOMBIE_WOLF, 15,
                    ZombieType.NORMAL_HARD, 15,
                    ZombieType.PIG_ZOMBIE, 15,
                    ZombieType.LITTLE_BOMBIE, 5,
                    ZombieType.BOMBIE, 1
            ), ZombieType.INFERNO)
    );

    public SpawnRate(final int spawnDelay, final Map<ZombieType, Integer> zombies) {
        this(spawnDelay, zombies, null);
    }

    public int getNumberOfZombies() {
        return zombies.values().stream().mapToInt(i -> i).sum();
    }
}