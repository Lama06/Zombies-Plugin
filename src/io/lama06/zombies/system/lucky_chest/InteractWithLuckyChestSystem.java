package io.lama06.zombies.system.lucky_chest;

import io.lama06.zombies.LuckyChest;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.pdc.EnumPersistentDataType;
import io.lama06.zombies.weapon.WeaponType;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

public final class InteractWithLuckyChestSystem implements Listener {
    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
            return;
        }
        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        final BlockPosition clickedBlockPos = clickedBlock.getLocation().toBlock();
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final ZombiesWorld world = player.getWorld();
        if (!world.isGameRunning()) {
            return;
        }
        final LuckyChest clickedLuckyChest = world.getConfig().luckyChests.stream()
                .filter(luckyChest -> luckyChest.position.equals(clickedBlockPos))
                .findAny().orElse(null);
        if (clickedLuckyChest == null) {
            return;
        }
        event.setCancelled(true);
        final ItemDisplay shuffleItem = getShuffleItem(clickedLuckyChest, world.getBukkit());
        if (shuffleItem == null) {
            openLuckyChest(player, world, clickedLuckyChest);
        } else {
            claimItem(player, world, shuffleItem);
        }
    }

    private void openLuckyChest(final ZombiesPlayer player, final ZombiesWorld world, final LuckyChest chest) {
        final int gold = player.get(ZombiesPlayer.GOLD);
        if (gold < chest.gold) {
            player.sendMessage(Component.text("You cannot afford opening this lucky chest").color(NamedTextColor.RED));
            return;
        }
        final FinePosition itemPosition = chest.getItemPosition(world.getBukkit());
        final ItemDisplay itemDisplay = world.getBukkit().spawn(itemPosition.toLocation(world.getBukkit()), ItemDisplay.class);
        final PersistentDataContainer pdc = itemDisplay.getPersistentDataContainer();
        pdc.set(LuckyChestItem.getRemainingTimeKey(), PersistentDataType.INTEGER, LuckyChestItem.SHUFFLE_TIME);
        final int newGold = gold - chest.gold;
        player.set(ZombiesPlayer.GOLD, newGold);
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, gold, newGold));
    }

    private void claimItem(final ZombiesPlayer player, final ZombiesWorld world, final ItemDisplay itemDisplay) {
        final PersistentDataContainer pdc = itemDisplay.getPersistentDataContainer();
        final Integer remainingTime = pdc.get(LuckyChestItem.getRemainingTimeKey(), PersistentDataType.INTEGER);
        if (remainingTime == null || remainingTime > 0) {
            return;
        }
        final WeaponType weaponType = pdc.get(LuckyChestItem.getWeaponKey(), new EnumPersistentDataType<>(WeaponType.class));
        if (weaponType == null) {
            return;
        }
        final PlayerInventory inventory = player.getBukkit().getInventory();
        final int slot = inventory.getHeldItemSlot();
        if (slot >= 3) {
            player.sendMessage(Component.text("Select a weapon slot").color(NamedTextColor.RED));
            return;
        }
        itemDisplay.remove();
        player.giveWeapon(slot, weaponType);
    }

    private ItemDisplay getShuffleItem(final LuckyChest chest, final World world) {
        final Collection<ItemDisplay> nearbyItemDisplays = chest.position.toLocation(world).getNearbyEntitiesByType(ItemDisplay.class, 5);
        for (final ItemDisplay itemDisplay : nearbyItemDisplays) {
            final PersistentDataContainer pdc = itemDisplay.getPersistentDataContainer();
            if (!pdc.has(LuckyChestItem.getRemainingTimeKey(), PersistentDataType.INTEGER)) {
                return null;
            }
            return itemDisplay;
        }
        return null;
    }
}
