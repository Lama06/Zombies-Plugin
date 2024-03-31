package io.lama06.zombies.system;

import io.lama06.zombies.*;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public final class DoorSystem implements Listener {
    @EventHandler
    private void closeDoorsOnStart(final GameStartEvent event) {
        final ZombiesWorld world = event.getWorld();
        final WorldConfig config = ZombiesPlugin.getConfig(world);
        for (final Door door : config.doors) {
            door.setOpen(world, false);
        }
        world.set(WorldAttributes.OPEN_DOORS, List.of());
        world.set(WorldAttributes.REACHABLE_AREAS, List.of(config.startArea));
    }

    @EventHandler
    private void openDoor(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !event.getAction().isRightClick()) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final World bukkitWorld = event.getPlayer().getWorld();
        if (!ZombiesWorld.isGameWorld(bukkitWorld)) {
            return;
        }
        final ZombiesWorld world = new ZombiesWorld(bukkitWorld);
        final WorldConfig config = ZombiesPlugin.getConfig(world);
        if (config == null) {
            return;
        }
        final int gold = player.get(PlayerAttributes.GOLD);
        final List<String> reachableAreas = world.get(WorldAttributes.REACHABLE_AREAS);
        final List<Integer> doorIndizes = world.get(WorldAttributes.OPEN_DOORS);
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
            world.showTitle(Title.title(Component.text(player.getBukkit().getName() + " opened a door"), Component.empty()));
            door.setOpen(world, true);
            player.set(PlayerAttributes.GOLD, gold - door.gold);
            final String newArea = reachableAreas.contains(door.area1) ? door.area2 : door.area1;
            final ArrayList<String> newAreas = new ArrayList<>(reachableAreas);
            newAreas.add(newArea);
            world.set(WorldAttributes.REACHABLE_AREAS, newAreas);

            final List<Integer> newDoorIndizes = new ArrayList<>(doorIndizes);
            newDoorIndizes.add(i);
            world.set(WorldAttributes.OPEN_DOORS, newDoorIndizes);
        }
    }
}