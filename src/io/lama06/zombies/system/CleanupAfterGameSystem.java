package io.lama06.zombies.system;

import io.lama06.zombies.*;
import io.lama06.zombies.event.GameEndEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class CleanupAfterGameSystem implements Listener {
    @EventHandler
    private void onGameEnd(final GameEndEvent event) {
        final ZombiesWorld world = event.getWorld();
        final WorldConfig config = world.getConfig();
        for (final Door door : config.doors) {
            door.setOpen(world, false);
        }
        if (config.powerSwitch != null) {
            config.powerSwitch.setActive(world, false);
        }
        for (final Window window : config.windows) {
            window.close(world);
        }
        world.remove(WorldAttributes.ROUND);
        world.remove(WorldAttributes.REMAINING_ZOMBIES);
        world.remove(WorldAttributes.NEXT_ZOMBIE_TIME);
        world.remove(WorldAttributes.REACHABLE_AREAS);
        world.remove(WorldAttributes.OPEN_DOORS);
        world.remove(WorldAttributes.POWER_SWITCH);

        for (final ZombiesPlayer player : world.getPlayers()) {
            player.remove(PlayerAttributes.GOLD);
            player.remove(PlayerAttributes.KILLS);
            final Player playerBukkit = player.getBukkit();
            playerBukkit.teleport(world.getBukkit().getSpawnLocation());
            playerBukkit.getInventory().clear();
        }
    }
}
