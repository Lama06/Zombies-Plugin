package io.lama06.zombies.system;

import io.lama06.zombies.WorldConfig;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.ZombiesWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class EnablePowerSwitchSystem implements Listener {
    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }
        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final ZombiesWorld world = player.getWorld();
        if (!world.isGameRunning()) {
            return;
        }
        final WorldConfig config = world.getConfig();
        if (config.powerSwitch == null) {
            return;
        }
        if (!clickedBlock.getLocation().toBlock().equals(config.powerSwitch.position)) {
            return;
        }
        final boolean powerSwitchOn = world.get(ZombiesWorld.POWER_SWITCH);
        if (powerSwitchOn) {
            player.sendMessage(Component.text("The power switch is already activated").color(NamedTextColor.RED));
            return;
        }
        if (!player.requireGold(config.powerSwitch.gold)) {
            return;
        }
        player.pay(config.powerSwitch.gold);
        world.showTitle(Title.title(Component.text(player.getBukkit().getName() + " activated the power switch"), Component.empty()));
        world.set(ZombiesWorld.POWER_SWITCH, true);
    }
}
