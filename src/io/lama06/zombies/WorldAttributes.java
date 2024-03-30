package io.lama06.zombies;

public final class WorldAttributes {
    public static final Attribute IS_GAME = new Attribute("is_game");

    public static final Attribute ROUND = new Attribute("round");

    public static final Attribute REMAINING_ZOMBIES = new Attribute("remaining_zombies");
    public static final Attribute NEXT_ZOMBIE_TIME = new Attribute("next_zombie");

    public static Attribute POWER_SWITCH = new Attribute("power_switch");
    public static final Attribute REACHABLE_AREAS = new Attribute("reachable_areas");
    public static final Attribute OPEN_DOORS = new Attribute("open_doors");
}
