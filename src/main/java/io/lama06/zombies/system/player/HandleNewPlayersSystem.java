package io.lama06.zombies.system.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.weapon.WeaponType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class HandleNewPlayersSystem implements Listener {
    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final Player bukkit = player.getBukkit();
        final Integer playerGameId = player.get(ZombiesPlayer.GAME_ID);
        final ZombiesWorld world = player.getWorld();
        if (!world.isGameRunning()) {
            bukkit.setGameMode(GameMode.ADVENTURE);
            bukkit.teleport(world.getBukkit().getSpawnLocation());
            bukkit.getInventory().clear();
            return;
        }
        final int worldGameId = world.get(ZombiesWorld.GAME_ID);
        if (playerGameId != null && playerGameId == worldGameId) {
            return;
        }
        bukkit.setGameMode(GameMode.ADVENTURE);
        bukkit.getInventory().clear();
        bukkit.teleport(world.getBukkit().getSpawnLocation());
        player.set(ZombiesPlayer.GAME_ID, worldGameId);
        player.set(ZombiesPlayer.GOLD, 0);
        player.set(ZombiesPlayer.KILLS, 0);
        player.giveWeapon(0, WeaponType.KNIFE);
        player.giveWeapon(1, WeaponType.PISTOL);
    }
}
