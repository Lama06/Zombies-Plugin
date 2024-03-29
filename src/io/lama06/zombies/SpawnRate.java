package io.lama06.zombies;

import io.lama06.zombies.zombie.ZombieType;

import java.util.List;
import java.util.Map;

public record SpawnRate(Map<ZombieType, Integer> zombies) {
    public static final List<SpawnRate> SPAWN_RATES = List.of(
            new SpawnRate(Map.ofEntries(
                    Map.entry(ZombieType.NORMAL_EASY, 3)
            )),
            new SpawnRate(Map.ofEntries(
                    Map.entry(ZombieType.NORMAL_EASY, 1),
                    Map.entry(ZombieType.NORMAL_MEDIUM, 2)
            )),
            new SpawnRate(Map.ofEntries(
                    Map.entry(ZombieType.NORMAL_HARD, 1)
            ))
    );
}
