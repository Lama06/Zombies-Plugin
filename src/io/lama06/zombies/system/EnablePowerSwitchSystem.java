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

public final class EnablePowerSwitchSystem implements Listener {
    @EventHandler
    private void handlePlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final World world = player.getWorld();
        final WorldConfig config = ZombiesPlugin.getConfig(world);
        if (config == null || config.powerSwitch == null) {
            return;
        }
        if (event.getClickedBlock() == null
                || !event.getClickedBlock().getLocation().toBlock().equals(config.powerSwitch.position)
                || !event.getAction().isRightClick()) {
            return;
        }
        final PersistentDataContainer playerPdc = player.getPersistentDataContainer();
        final PersistentDataContainer worldPdc = world.getPersistentDataContainer();
        final Boolean powerSwitchOn = worldPdc.get(WorldAttributes.POWER_SWITCH.getKey(), PersistentDataType.BOOLEAN);
        final Integer gold = playerPdc.get(PlayerAttributes.GOLD.getKey(), PersistentDataType.INTEGER);
        if (powerSwitchOn == null || gold == null) {
            return;
        }
        if (powerSwitchOn) {
            return;
        }
        if (gold < config.powerSwitch.gold) {
            player.sendMessage(Component.text("You don't have enough gold to open this door").color(NamedTextColor.RED));
            return;
        }
        playerPdc.set(PlayerAttributes.GOLD.getKey(), PersistentDataType.INTEGER, gold - config.powerSwitch.gold);
        world.showTitle(Title.title(Component.text(player.getName() + " activated the power switch"), Component.empty()));
        worldPdc.set(WorldAttributes.POWER_SWITCH.getKey(), PersistentDataType.BOOLEAN, true);
    }
}
