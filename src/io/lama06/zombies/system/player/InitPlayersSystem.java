package io.lama06.zombies.system.player;

import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.WeaponType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class InitPlayersSystem implements Listener {
    @EventHandler
    private void onGameStart(final GameStartEvent event) {
        final List<ZombiesPlayer> players = event.getWorld().getPlayers();
        for (final ZombiesPlayer player : players) {
            final Player bukkit = player.getBukkit();
            bukkit.getInventory().clear();
            bukkit.setFoodLevel(20);
            bukkit.setHealth(20);
            bukkit.setGameMode(GameMode.ADVENTURE);
            bukkit.setLevel(0);
            bukkit.setExp(0);
            player.giveWeapon(0, WeaponType.KNIFE);
            player.giveWeapon(1, WeaponType.PISTOL);
            player.set(PlayerAttributes.KILLS, 0);
            player.set(PlayerAttributes.GOLD, 0);
        }
    }
}
