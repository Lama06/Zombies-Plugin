package io.lama06.zombies.system;

import io.lama06.zombies.*;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.weapon.WeaponType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class StartGameSystem implements Listener {
    @EventHandler
    private void onStart(final GameStartEvent event) {
        final World world = event.getWorld();
        final WorldConfig config = ZombiesPlugin.getConfig(world);
        if (config == null) {
            return;
        }
        for (final Window window : config.windows) {
            window.close(world);
        }
        for (final Player player : world.getPlayers()) {
            player.getInventory().clear();
            player.setFoodLevel(20);
            player.setHealth(20);
            final ItemStack knife = WeaponType.KNIFE.data.createWeapon();
            player.getInventory().setItem(0, knife);
            final ItemStack pistol = WeaponType.PISTOL.data.createWeapon();
            player.getInventory().setItem(1, pistol);
        }
        final SpawnRate spawnRate = SpawnRate.SPAWN_RATES.getFirst();
        final PersistentDataContainer pdc = world.getPersistentDataContainer();
        pdc.set(WorldAttributes.IS_GAME.getKey(), PersistentDataType.BOOLEAN, true);
        pdc.set(WorldAttributes.NEXT_ZOMBIE_TIME.getKey(), PersistentDataType.INTEGER, spawnRate.spawnDelay());
        pdc.set(WorldAttributes.ROUND.getKey(), PersistentDataType.INTEGER, 1);
        pdc.set(WorldAttributes.POWER_SWITCH.getKey(), PersistentDataType.BOOLEAN, false);
        pdc.set(WorldAttributes.REMAINING_ZOMBIES.getKey(), PersistentDataType.INTEGER, spawnRate.getNumberOfZombies());
        pdc.set(WorldAttributes.REACHABLE_AREAS.getKey(), PersistentDataType.LIST.strings(), List.of(config.startArea));
        pdc.set(WorldAttributes.OPEN_DOORS.getKey(), PersistentDataType.LIST.integers(), List.of());
    }
}
