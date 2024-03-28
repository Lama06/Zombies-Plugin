package io.lama06.zombies.weapon.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponReloadChangeEvent;
import io.lama06.zombies.weapon.event.WeaponRenderItemEvent;
import io.lama06.zombies.weapon.event.WeaponRenderItemRequestEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public final class ReloadRenderSystem extends System {
    public ReloadRenderSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onReloadChangeEvent(final WeaponReloadChangeEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new WeaponRenderItemRequestEvent(event.getWeapon()));
    }

    @EventHandler
    private void onWeaponRender(final WeaponRenderItemEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final ItemStack item = event.getItem();
        final ItemMeta meta = item.getItemMeta();
        if (meta instanceof final Damageable damageable && weapon.getReload() != null && !weapon.getReload().isReady()) {
            final double reloadProgress = (double) weapon.getReload().getRemainingReload() / weapon.getReload().getReload();
            final int durability = (int) (reloadProgress * item.getType().getMaxDurability());
            damageable.setDamage(item.getType().getMaxDurability() - durability);
        }
        item.setItemMeta(meta);
    }
}
