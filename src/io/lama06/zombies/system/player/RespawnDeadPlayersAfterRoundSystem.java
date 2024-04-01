package io.lama06.zombies.system.player;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.StartRoundEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class RespawnDeadPlayersAfterRoundSystem implements Listener {
    @EventHandler
    private void onRoundStart(final StartRoundEvent event) {
        final ZombiesWorld world = event.getWorld();
        for (final ZombiesPlayer player : world.getPlayers()) {
            final Player bukkit = player.getBukkit();
            if (bukkit.getGameMode() != GameMode.SPECTATOR) {
                continue;
            }
            bukkit.setGameMode(GameMode.ADVENTURE);
            bukkit.teleport(world.getBukkit().getSpawnLocation());
            bukkit.setHealth(20);
        }
    }
}
