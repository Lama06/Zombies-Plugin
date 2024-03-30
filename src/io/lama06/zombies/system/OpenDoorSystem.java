package io.lama06.zombies.system;

import io.lama06.zombies.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public final class OpenDoorSystem implements Listener {
    @EventHandler
    private void handlePlayerInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !event.getAction().isRightClick()) {
            return;
        }
        final Player player = event.getPlayer();
        final World world = player.getWorld();
        final WorldConfig config = ZombiesPlugin.getConfig(world);
        if (config == null) {
            return;
        }
        final PersistentDataContainer playerPdc = player.getPersistentDataContainer();
        final PersistentDataContainer worldPdc = world.getPersistentDataContainer();
        final Integer gold = playerPdc.get(PlayerAttributes.GOLD.getKey(), PersistentDataType.INTEGER);
        final List<String> areas = worldPdc.get(WorldAttributes.REACHABLE_AREAS.getKey(), PersistentDataType.LIST.strings());
        final List<Integer> doorIndizes = worldPdc.get(WorldAttributes.OPEN_DOORS.getKey(), PersistentDataType.LIST.integers());
        if (gold == null || areas == null || doorIndizes == null) {
            return;
        }
        for (int i = 0; i < config.doors.size(); i++) {
            final Door door = config.doors.get(i);
            if (doorIndizes.contains(i)) {
                continue;
            }
            if (!door.activationBlock.equals(event.getClickedBlock().getLocation().toBlock())) {
                continue;
            }
            if (gold < door.gold) {
                player.sendMessage(Component.text("You don't have enough gold to open this door").color(NamedTextColor.RED));
                return;
            }
            world.showTitle(Title.title(Component.text(player.getName() + " opened a door"), Component.empty()));
            door.setOpen(world, true);

            playerPdc.set(PlayerAttributes.GOLD.getKey(), PersistentDataType.INTEGER, gold - door.gold);

            final String newArea = areas.contains(door.area1) ? door.area2 : door.area1;
            final ArrayList<String> newAreas = new ArrayList<>(areas);
            newAreas.add(newArea);
            worldPdc.set(WorldAttributes.REACHABLE_AREAS.getKey(), PersistentDataType.LIST.strings(), newAreas);

            final List<Integer> newDoorIndizes = new ArrayList<>(doorIndizes);
            newDoorIndizes.add(i);
            worldPdc.set(WorldAttributes.OPEN_DOORS.getKey(), PersistentDataType.LIST.integers(), newDoorIndizes);
        }
    }
}
