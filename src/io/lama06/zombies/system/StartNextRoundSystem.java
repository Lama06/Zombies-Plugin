package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.SpawnRate;
import io.lama06.zombies.WorldAttributes;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.StartRoundEvent;
import io.lama06.zombies.zombie.Zombie;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class StartNextRoundSystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getWorlds()) {
            final List<Zombie> zombies = world.getZombies();
            if (!zombies.isEmpty()) {
                continue;
            }
            final int currentRound = world.get(WorldAttributes.ROUND);
            final int remainingZombies = world.get(WorldAttributes.REMAINING_ZOMBIES);
            if (remainingZombies > 0) {
                continue;
            }
            final int nextRound = currentRound + 1;
            if (nextRound > SpawnRate.SPAWN_RATES.size()) {
                world.sendMessage(Component.text("Finish"));
                continue;
            }
            final SpawnRate spawnRate = SpawnRate.SPAWN_RATES.get(nextRound - 1);
            world.set(WorldAttributes.ROUND, nextRound);
            world.set(WorldAttributes.NEXT_ZOMBIE_TIME, spawnRate.spawnDelay());
            world.set(WorldAttributes.REMAINING_ZOMBIES, spawnRate.getNumberOfZombies());
            world.sendMessage(Component.text("Round " + nextRound));
            Bukkit.getPluginManager().callEvent(new StartRoundEvent(world, currentRound + 1));
        }
    }
}
