package io.lama06.zombies.system;

import io.lama06.zombies.*;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.WeaponType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class PrepareWorldAtGameStartSystem implements Listener {
    @EventHandler
    private void onGameStart(final GameStartEvent event) {
        final ZombiesWorld world = event.getWorld();
        final WorldConfig config = world.getConfig();

        for (final Door door : config.doors) {
            door.setOpen(world, false);
        }
        world.set(WorldAttributes.OPEN_DOORS, List.of());
        world.set(WorldAttributes.REACHABLE_AREAS, List.of(config.startArea));

        for (final Window window : config.windows) {
            window.close(event.getWorld());
        }

        if (config.powerSwitch != null) {
            config.powerSwitch.setActive(world, false);
        }
        world.set(WorldAttributes.POWER_SWITCH, false);

        final SpawnRate firstRoundSpawnRate = SpawnRate.SPAWN_RATES.getFirst();
        world.set(WorldAttributes.NEXT_ZOMBIE_TIME, firstRoundSpawnRate.spawnDelay());
        world.set(WorldAttributes.REMAINING_ZOMBIES, firstRoundSpawnRate.getNumberOfZombies());
        event.getWorld().set(WorldAttributes.ROUND, 1);

        for (final ZombiesPlayer player : world.getPlayers()) {
            final Player bukkit = player.getBukkit();
            bukkit.getInventory().clear();
            bukkit.teleport(world.getBukkit().getSpawnLocation());
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
