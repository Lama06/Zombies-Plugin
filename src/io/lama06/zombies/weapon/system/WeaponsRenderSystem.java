package io.lama06.zombies.weapon.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponRenderItemEvent;
import io.lama06.zombies.weapon.event.WeaponRenderItemRequestEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class WeaponsRenderSystem extends System {
    public WeaponsRenderSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onGameStart(final GameStartEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        for (final ZombiesPlayer player : game.getPlayers().values()) {
            renderWeapons(player);
        }
    }

    @EventHandler
    private void onWeaponRenderRequest(final WeaponRenderItemRequestEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        renderWeapons(event.getPlayer());
    }

    private void renderWeapons(final ZombiesPlayer player) {
        for (int i = 0; i < player.getWeapons().size(); i++) {
            final Weapon weapon = player.getWeapons().get(i);
            final ItemStack item = createWeaponItem(weapon);
            player.getBukkit().getInventory().setItem(i, item);
        }
    }

    private ItemStack createWeaponItem(final Weapon weapon) {
        final ItemStack item = new ItemStack(weapon.getMaterial());
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(weapon.getDisplayName());
        item.setItemMeta(meta);
        Bukkit.getPluginManager().callEvent(new WeaponRenderItemEvent(weapon, item));
        return item;
    }
}
