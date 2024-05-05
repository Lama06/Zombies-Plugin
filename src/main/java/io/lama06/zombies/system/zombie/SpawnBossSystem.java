package io.lama06.zombies.system.zombie;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.SpawnRate;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class SpawnBossSystem implements Listener {
    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld gameWorld : ZombiesPlugin.INSTANCE.getGameWorlds()) {
            final int currentRound = gameWorld.get(ZombiesWorld.ROUND);
            final SpawnRate currentSpawnRate = SpawnRate.SPAWN_RATES.get(currentRound - 1);
            if (currentSpawnRate.boss() == null) {
                continue;
            }
            final int remainingZombies = gameWorld.get(ZombiesWorld.REMAINING_ZOMBIES);
            if (remainingZombies > currentSpawnRate.getNumberOfZombies() / 2) {
                continue;
            }
            final boolean bossSpawned = gameWorld.get(ZombiesWorld.BOSS_SPAWNED);
            if (bossSpawned) {
                continue;
            }
            final Location spawnPoint = gameWorld.getNextZombieSpawnPoint();
            gameWorld.spawnZombie(spawnPoint, currentSpawnRate.boss());
            final String bossName = currentSpawnRate.boss().name().replace('_', ' ');
            gameWorld.sendMessage(Component.text(bossName + " has spawned!").color(NamedTextColor.RED));
            gameWorld.set(ZombiesWorld.BOSS_SPAWNED, true);
        }
    }
}
