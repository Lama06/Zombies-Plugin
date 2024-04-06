package io.lama06.zombies.system.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.weapon.WeaponType;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class HandleNewPlayersSystem implements Listener {
    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final ZombiesWorld world = player.getWorld();
        if (!world.isGameRunning()) {
            return;
        }
        player.getBukkit().setGameMode(GameMode.SPECTATOR);
        player.set(ZombiesPlayer.GOLD, 0);
        player.set(ZombiesPlayer.KILLS, 0);
        player.getBukkit().getInventory().clear();
        player.giveWeapon(0, WeaponType.KNIFE);
        player.giveWeapon(1, WeaponType.PISTOL);
    }
}
