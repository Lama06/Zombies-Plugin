package io.lama06.zombies.system;

import io.lama06.zombies.*;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.ZombiesPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public final class OpenDoorSystem implements Listener {
    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !event.getAction().isRightClick()) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final ZombiesWorld world = player.getWorld();
        if (!world.isGameRunning() || !player.isAlive()) {
            return;
        }
        final WorldConfig config = world.getConfig();
        if (config == null) {
            return;
        }
        final int gold = player.get(ZombiesPlayer.GOLD);
        final List<String> reachableAreas = world.get(ZombiesWorld.REACHABLE_AREAS);
        final List<Integer> doorIndizes = world.get(ZombiesWorld.OPEN_DOORS);
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
            player.set(ZombiesPlayer.GOLD, gold - door.gold);
            Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, gold, gold - door.gold));

            final String newArea = reachableAreas.contains(door.area1) ? door.area2 : door.area1;
            final ArrayList<String> newAreas = new ArrayList<>(reachableAreas);
            newAreas.add(newArea);
            world.set(ZombiesWorld.REACHABLE_AREAS, newAreas);

            final List<Integer> newDoorIndizes = new ArrayList<>(doorIndizes);
            newDoorIndizes.add(i);
            world.set(ZombiesWorld.OPEN_DOORS, newDoorIndizes);
        }
    }
}
