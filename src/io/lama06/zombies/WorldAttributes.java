package io.lama06.zombies;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class WorldAttributes {
    public static final AttributeId<Boolean> GAME_RUNNING = new AttributeId<>("is_game", PersistentDataType.BOOLEAN);

    public static final AttributeId<Integer> ROUND = new AttributeId<>("round", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> REMAINING_ZOMBIES = new AttributeId<>("remaining_zombies", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> NEXT_ZOMBIE_TIME = new AttributeId<>("next_zombie", PersistentDataType.INTEGER);

    public static final AttributeId<Boolean> POWER_SWITCH = new AttributeId<>("power_switch", PersistentDataType.BOOLEAN);
    public static final AttributeId<List<String>> REACHABLE_AREAS = new AttributeId<>("reachable_areas", PersistentDataType.LIST.strings());
    public static final AttributeId<List<Integer>> OPEN_DOORS = new AttributeId<>("open_doors", PersistentDataType.LIST.integers());
}
