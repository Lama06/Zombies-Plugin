package io.lama06.zombies;

import io.lama06.zombies.zombie.ZombieType;

import java.util.List;
import java.util.Map;

public record SpawnRate(int spawnDelay, Map<ZombieType, Integer> zombies) {
    public static final List<SpawnRate> SPAWN_RATES = List.of(
            new SpawnRate(2 * 20, Map.ofEntries(
                    Map.entry(ZombieType.NORMAL_EASY, 1)
            )),
            new SpawnRate(5 * 20, Map.ofEntries(
                    Map.entry(ZombieType.NORMAL_EASY, 1)
                 //   Map.entry(ZombieType.NORMAL_MEDIUM, 2)
            ))
//            new SpawnRate(3 * 20, Map.ofEntries(
//                    Map.entry(ZombieType.NORMAL_HARD, 1)
//            ))
    );

    public int getNumberOfZombies() {
        return zombies.values().stream().mapToInt(i -> i).sum();
    }
}