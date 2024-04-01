package io.lama06.zombies.system.player;

import io.lama06.zombies.event.player.PlayerKillZombieEvent;
import io.lama06.zombies.event.player.PlayerKillsIncrementEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class IncrementPlayerKillsSystem implements Listener {
    @EventHandler
    private void onPlayerKillsZombie(final PlayerKillZombieEvent event) {
        final ZombiesPlayer player = event.getPlayer();
        final int kills = player.get(PlayerAttributes.KILLS);
        player.set(PlayerAttributes.KILLS, kills + 1);
        Bukkit.getPluginManager().callEvent(new PlayerKillsIncrementEvent(player, kills + 1));
    }
}
