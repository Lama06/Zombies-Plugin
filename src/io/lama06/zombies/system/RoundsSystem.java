package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.SpawnRate;
import io.lama06.zombies.WorldAttributes;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.event.StartRoundEvent;
import io.lama06.zombies.zombie.Zombie;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class RoundsSystem implements Listener {
    @EventHandler
    private void initRounds(final GameStartEvent event) {
        final PersistentDataContainer pdc = event.getWorld().getPersistentDataContainer();
        pdc.set(WorldAttributes.ROUND.getKey(), PersistentDataType.INTEGER, 1);
    }

    @EventHandler
    private void startNextRound(final ServerTickEndEvent event) {
        for (final World world : ZombiesWorld.getGameWorlds()) {
            final List<Entity> zombies = Zombie.getZombiesInWorld(world);
            if (!zombies.isEmpty()) {
                return;
            }
            final PersistentDataContainer pdc = world.getPersistentDataContainer();
            final Integer currentRound = pdc.get(WorldAttributes.ROUND.getKey(), PersistentDataType.INTEGER);
            final Integer remainingZombies = pdc.get(WorldAttributes.REMAINING_ZOMBIES.getKey(), PersistentDataType.INTEGER);
            if (currentRound == null || remainingZombies == null || remainingZombies != 0) {
                continue;
            }
            final int nextRound = currentRound + 1;
            if (nextRound > SpawnRate.SPAWN_RATES.size()) {
                world.sendMessage(Component.text("Finish"));
                return;
            }
            final SpawnRate spawnRate = SpawnRate.SPAWN_RATES.get(nextRound - 1);
            pdc.set(WorldAttributes.ROUND.getKey(), PersistentDataType.INTEGER, nextRound);
            pdc.set(WorldAttributes.NEXT_ZOMBIE_TIME.getKey(), PersistentDataType.INTEGER, spawnRate.spawnDelay());
            pdc.set(WorldAttributes.REMAINING_ZOMBIES.getKey(), PersistentDataType.INTEGER, spawnRate.getNumberOfZombies());
            world.sendMessage(Component.text("Round " + nextRound));
            Bukkit.getPluginManager().callEvent(new StartRoundEvent(world, currentRound + 1));
        }
    }
}
