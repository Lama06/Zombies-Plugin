package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.System;
import io.lama06.zombies.Window;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieType;
import io.lama06.zombies.zombie.event.ZombieSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class ZombieSpawnSystem extends System {
    public ZombieSpawnSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        if (game.getNextZombieTicks() > 0) {
            game.setNextZombieTicks(game.getNextZombieTicks() - 1);
        }
        if (game.getNextZombieTicks() != 0) {
            return;
        }
        game.setNextZombieTicks(10*20);

        final ZombieType type = getNextZombie();
        if (type == null) {
            return;
        }

        final Location spawnPoint = getNextSpawnPoint();
        final Entity entity = world.spawnEntity(spawnPoint, type.data.entity(), false);
        final Zombie zombie = new Zombie(game, entity, type.data);
        game.getZombies().put(entity, zombie);
        Bukkit.getPluginManager().callEvent(new ZombieSpawnEvent(zombie));
    }

    private ZombieType getNextZombie() {
        if (game.getRemainingZombies().isEmpty()) {
            return null;
        }
        final List<ZombieType> zombieTypes = new ArrayList<>();
        game.getRemainingZombies().forEach((zombie, number) -> {
            for (int i = 0; i < number; i++) {
                zombieTypes.add(zombie);
            }
        });
        final ThreadLocalRandom rnd = ThreadLocalRandom.current();
        final ZombieType nextZombieType = zombieTypes.get(rnd.nextInt(zombieTypes.size()));
        final int number = game.getRemainingZombies().get(nextZombieType);
        if (number == 1) {
            game.getRemainingZombies().remove(nextZombieType);
        } else {
            game.getRemainingZombies().put(nextZombieType, number-1);
        }
        return nextZombieType;
    }

    private List<Window> getAvailableWindows() {
        return game.getConfig().windows.stream()
                .filter(window -> game.getReachableAreas().contains(window.area))
                .toList();
    }

    private Window getNearestWindowToPlayer(final ZombiesPlayer player) {
        final List<Window> windows = getAvailableWindows();
        return windows.stream().min(Comparator.comparingDouble(window -> window.spawnLocation.coordinates().toVector()
                    .distance(player.getBukkit().getLocation().toVector()))).orElseThrow();

    }

    private Location getNextSpawnPoint() {
        final ThreadLocalRandom rnd = ThreadLocalRandom.current();
        final List<ZombiesPlayer> players = new ArrayList<>(game.getPlayers().values());
        final ZombiesPlayer player = players.get(rnd.nextInt(players.size()));
        final Window window = getNearestWindowToPlayer(player);
        return window.spawnLocation.toBukkit(game.getWorld());
    }
}
