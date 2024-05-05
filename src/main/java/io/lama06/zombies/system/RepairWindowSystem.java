package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.*;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.perk.GlobalPerk;
import io.lama06.zombies.zombie.Zombie;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RepairWindowSystem implements Listener {
    private static final int DELAY = 30;
    private static final int GOLD = 10;

    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        if (Bukkit.getCurrentTick() % DELAY != 0) {
            return;
        }
        final List<ZombiesPlayer> players = ZombiesPlugin.INSTANCE.getAlivePlayers();
        for (final ZombiesPlayer player : players) {
            if (!player.getBukkit().isSneaking()) {
                continue;
            }
            final Location playerLocation = player.getBukkit().getLocation();
            final ZombiesWorld world = player.getWorld();
            final WorldConfig config = world.getConfig();
            for (final Window window : config.windows) {
                if (!window.repairArea.containsBlock(playerLocation.toBlock())) {
                    continue;
                }
                if (player.getBukkit().getNearbyEntities(4, 4, 4).stream().map(Zombie::new).anyMatch(Zombie::isZombie)) {
                    player.sendMessage(Component.text("Zombies are nearby. The window can't be repaired.").color(NamedTextColor.RED));
                    break;
                }
                repairWindow(world, player, window);
                break;
            }
        }
    }

    private void repairWindow(final ZombiesWorld world, final ZombiesPlayer player, final Window window) {
        for (final BlockPosition blockPos : window.blocks.getBlocks()) {
            final Block block = blockPos.toLocation(world.getBukkit()).getBlock();
            if (block.getType() != Material.AIR) {
                continue;
            }
            block.setType(Material.OAK_SLAB);
            world.getBukkit().playSound(block.getLocation(), Sound.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
            final int gold = player.get(ZombiesPlayer.GOLD);
            final int goldAdd = (world.isPerkEnabled(GlobalPerk.DOUBLE_GOLD) ? 2 : 1) * GOLD;
            final int newGold = gold + goldAdd;
            player.set(ZombiesPlayer.GOLD, newGold);
            Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, gold, newGold));
            player.sendMessage(Component.text("Window Repaired: +%s Gold".formatted(goldAdd)).color(NamedTextColor.GOLD));
            return;
        }
    }
}
