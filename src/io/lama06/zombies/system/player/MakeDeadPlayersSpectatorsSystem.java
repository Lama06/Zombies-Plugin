package io.lama06.zombies.system.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.ZombiesWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class MakeDeadPlayersSpectatorsSystem implements Listener {
    @EventHandler
    private void onPlayerDies(final PlayerDeathEvent event) {
        final Player bukkitPlayer = event.getPlayer();
        final ZombiesPlayer player = new ZombiesPlayer(bukkitPlayer);
        final ZombiesWorld world = new ZombiesWorld(bukkitPlayer.getWorld());
        if (!world.isGameRunning()) {
            return;
        }
        player.clearPerks();
        bukkitPlayer.setGameMode(GameMode.SPECTATOR);
        event.setKeepInventory(true);
        bukkitPlayer.getWorld().sendMessage(bukkitPlayer.displayName().append(Component.text(" died").color(NamedTextColor.RED)));
    }
}
