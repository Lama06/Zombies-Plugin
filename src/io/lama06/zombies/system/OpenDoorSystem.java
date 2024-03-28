package io.lama06.zombies.system;

import io.lama06.zombies.Door;
import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public final class OpenDoorSystem extends System {
    public OpenDoorSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void handlePlayerInteract(final PlayerInteractEvent event) {
        if (!game.getPlayers().containsKey(event.getPlayer())) {
            return;
        }
        final ZombiesPlayer player = game.getPlayers().get(event.getPlayer());
        if (event.getClickedBlock() == null || !event.getAction().isRightClick()) {
            return;
        }
        for (final Door door : game.getConfig().doors) {
            if (game.getOpenDoors().contains(door)) {
                continue;
            }
            if (!door.activationBlock.equals(event.getClickedBlock().getLocation().toBlock())) {
                continue;
            }
            if (player.getGold() < door.gold) {
                event.getPlayer().sendMessage(Component.text("You don't have enough gold to open this door").color(NamedTextColor.RED));
                return;
            }
            player.setGold(player.getGold() - door.gold);
            door.setOpen(game.getWorld(), true);
            game.showTitle(Title.title(Component.text(event.getPlayer().getName() + " opened a door"), Component.empty()));
            final String newArea = game.getReachableAreas().contains(door.area1) ? door.area2 : door.area1;
            game.getReachableAreas().add(newArea);
        }
    }
}
