package io.lama06.zombies.system;

import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class InitPlayersSystem implements Listener {
    @EventHandler
    private void onGameStart(final GameStartEvent event) {
        final List<ZombiesPlayer> players = event.getWorld().getPlayers();
        for (final ZombiesPlayer player : players) {
            player.set(PlayerAttributes.KILLS, 0);
            player.set(PlayerAttributes.GOLD, 0);
        }
    }
}
