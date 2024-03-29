package io.lama06.zombies.system;

import io.lama06.zombies.*;
import io.lama06.zombies.System;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public final class EnablePowerSwitchSystem extends System {
    public EnablePowerSwitchSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void handlePlayerInteract(final PlayerInteractEvent event) {
        if (game.isPowerSwitchEnabled()) {
            return;
        }
        final PowerSwitch powerSwitch = game.getConfig().powerSwitch;
        if (powerSwitch == null) {
            return;
        }
        if (event.getClickedBlock() == null
                || !event.getClickedBlock().getLocation().toBlock().equals(powerSwitch.position)
                || !event.getAction().isRightClick()) {
            return;
        }
        if (!game.getPlayers().containsKey(event.getPlayer())) {
            return;
        }
        final ZombiesPlayer player = game.getPlayers().get(event.getPlayer());
        if (player.getGold() < powerSwitch.gold) {
            event.getPlayer().sendMessage(Component.text("You don't have enough gold to open this door").color(NamedTextColor.RED));
            return;
        }
        player.setGold(player.getGold() - powerSwitch.gold);
        game.showTitle(Title.title(Component.text(event.getPlayer().getName() + " activated the power switch"), Component.empty()));
        game.setPowerSwitchEnabled(true);
    }
}
