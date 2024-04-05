package io.lama06.zombies.system.perk.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.perk.PerkMachine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class BuyPerkSystem implements Listener {
    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
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
        final PerkMachine perkMachine = world.getConfig().perkMachines.stream()
                .filter(machine -> machine.position.equals(clickedBlock.getLocation().toBlock()))
                .findAny().orElse(null);
        if (perkMachine == null) {
            return;
        }
        if (player.hasPerk(perkMachine.perk)) {
            player.sendMessage(Component.text("You already own this perk").color(NamedTextColor.RED));
            return;
        }
        if (!player.requireGold(perkMachine.gold)) {
            return;
        }
        final int slot = player.getBukkit().getInventory().getHeldItemSlot();
        if (slot < 6 || slot >= 9) {
            player.sendMessage(Component.text("Select a valid perk slot").color(NamedTextColor.RED));
            return;
        }
        player.givePerk(slot, perkMachine.perk);
        player.pay(perkMachine.gold);
        player.sendMessage(Component.text("Successfully bought ").color(NamedTextColor.GREEN).append(perkMachine.perk.getDisplayName()));
    }
}
