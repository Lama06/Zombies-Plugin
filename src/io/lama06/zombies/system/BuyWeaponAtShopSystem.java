package io.lama06.zombies.system;

import io.lama06.zombies.WeaponShop;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class BuyWeaponAtShopSystem implements Listener {
    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
            return;
        }
        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        final Player bukkit = event.getPlayer();
        if (!ZombiesPlayer.isZombiesPlayer(bukkit)) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(bukkit);
        final WeaponShop weaponShop = ZombiesPlugin.getConfig(player.getWorld()).weaponShops.stream()
                .filter(shop -> shop.position.equals(clickedBlock.getLocation().toBlock()))
                .findAny()
                .orElse(null);
        if (weaponShop == null) {
            return;
        }
        final Weapon heldWeapon = player.getHeldWeapon();
        if (heldWeapon != null) {
            return;
        }
        final int gold = player.get(PlayerAttributes.GOLD);
        if (gold < weaponShop.purchasePrice) {
            return;
        }
        final int slot = bukkit.getInventory().getHeldItemSlot();
        if (slot >= 3) {
            return;
        }
        player.giveWeapon(slot, weaponShop.weaponType.data);
        player.set(PlayerAttributes.GOLD, gold - weaponShop.purchasePrice);
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, gold, gold - weaponShop.purchasePrice));
    }
}
