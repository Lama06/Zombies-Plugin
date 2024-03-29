package io.lama06.zombies;

import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public final class ZombiesGame implements ForwardingAudience {
    private final World world;
    private final WorldConfig config;

    private final List<System> systems = new ArrayList<>();

    private final Map<Player, ZombiesPlayer> players = new HashMap<>();
    private final Set<String> reachableAreas = new HashSet<>();
    private final Set<Door> openDoors = new HashSet<>();
    private boolean powerSwitchEnabled;

    private int round = 1;
    private Map<ZombieType, Integer> remainingZombies = new HashMap<>(SpawnRate.SPAWN_RATES.getFirst().zombies());
    private int nextZombieTicks;
    private final Map<Entity, Zombie> zombies = new HashMap<>();

    public ZombiesGame(final World world, final WorldConfig config) {
        this.world = world;
        this.config = config;
        reachableAreas.add(config.startArea);

        for (final Player player : world.getPlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue;
            }
            player.getInventory().clear();
            player.setFoodLevel(20);
            players.put(player, new ZombiesPlayer(this, player));
        }
        for (final Function<ZombiesGame, System> constructor : Systems.SYSTEMS) {
            final System system = constructor.apply(this);
            systems.add(system);
            Bukkit.getPluginManager().registerEvents(system, ZombiesPlugin.INSTANCE);
        }
        for (final Window window : config.windows) {
            window.close(world);
        }
        Bukkit.getPluginManager().callEvent(new GameStartEvent(this));
    }

    public void cleanup() {
        for (final System system : systems) {
            HandlerList.unregisterAll(system);
        }
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return players.keySet();
    }

    public World getWorld() {
        return world;
    }

    public WorldConfig getConfig() {
        return config;
    }

    public Map<Player, ZombiesPlayer> getPlayers() {
        return players;
    }

    public Set<String> getReachableAreas() {
        return reachableAreas;
    }

    public Set<Door> getOpenDoors() {
        return openDoors;
    }

    public boolean isPowerSwitchEnabled() {
        return powerSwitchEnabled;
    }

    public void setPowerSwitchEnabled(final boolean powerSwitchEnabled) {
        this.powerSwitchEnabled = powerSwitchEnabled;
    }

    public int getRound() {
        return round;
    }

    public void setRound(final int round) {
        this.round = round;
    }

    public void setRemainingZombies(final Map<ZombieType, Integer> remainingZombies) {
        this.remainingZombies = remainingZombies;
    }

    public Map<ZombieType, Integer> getRemainingZombies() {
        return remainingZombies;
    }

    public Map<Entity, Zombie> getZombies() {
        return zombies;
    }

    public int getNextZombieTicks() {
        return nextZombieTicks;
    }

    public void setNextZombieTicks(final int nextZombieTicks) {
        this.nextZombieTicks = nextZombieTicks;
    }
}
