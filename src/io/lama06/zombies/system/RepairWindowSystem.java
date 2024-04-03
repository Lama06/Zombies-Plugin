package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.*;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RepairWindowSystem implements Listener {
    private static final int DELAY = 3 * 20;
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
            player.sendMessage(Component.text("Window Repaired: +10 Gold").color(NamedTextColor.GOLD));
            world.getBukkit().playSound(block.getLocation(), Sound.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
            final int gold = player.get(PlayerAttributes.GOLD);
            final int newGold = gold + (world.isPerkEnabled(GlobalPerk.DOUBLE_GOLD) ? 2 : 1) * GOLD;
            player.set(PlayerAttributes.GOLD, newGold);
            Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, gold, newGold));
            return;
        }
    }
}
