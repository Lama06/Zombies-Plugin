package io.lama06.zombies;

import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.weapon.WeaponType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class ZombiesWorld {
    public static boolean isGameWorld(final World world) {
        return world.getPersistentDataContainer().getOrDefault(WorldAttributes.IS_GAME.getKey(), PersistentDataType.BOOLEAN, false);
    }

    public static List<World> getGameWorlds() {
        return Bukkit.getWorlds().stream().filter(ZombiesWorld::isGameWorld).toList();
    }

    public static void startGame(final World world) {
        final WorldConfig config = ZombiesPlugin.getConfig(world);
        if (config == null) {
            return;
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
        final PersistentDataContainer pdc = world.getPersistentDataContainer();
        pdc.set(WorldAttributes.IS_GAME.getKey(), PersistentDataType.BOOLEAN, true);
        Bukkit.getPluginManager().callEvent(new GameStartEvent(world));
    }
}
