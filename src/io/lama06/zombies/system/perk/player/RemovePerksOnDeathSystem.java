package io.lama06.zombies.system.perk.player;

import io.lama06.zombies.ZombiesPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class RemovePerksOnDeathSystem implements Listener {
    @EventHandler
    private void onPlayerDeath(final PlayerDeathEvent event) {
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        if (!player.getWorld().isGameRunning()) {
            return;
        }
        player.clearPerks();
    }
}
