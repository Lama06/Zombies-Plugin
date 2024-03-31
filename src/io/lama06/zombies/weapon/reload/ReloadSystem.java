package io.lama06.zombies.weapon.reload;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.ammo.AmmoAttributes;
import io.lama06.zombies.weapon.ammo.WeaponClipChangeEvent;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.shoot.WeaponShootEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class ReloadSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final Integer reload = event.getData().reload();
        if (reload == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component reloadComponent = weapon.addComponent(WeaponComponents.RELOAD);
        reloadComponent.set(ReloadAttributes.RELOAD, reload);
        reloadComponent.set(ReloadAttributes.REMAINING_RELOAD, 0);
    }

    @EventHandler(ignoreCancelled = true)
    private void preventShootDuringReload(final WeaponShootEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        if (reloadComponent == null) {
            return;
        }
        final int remainingReload = reloadComponent.get(ReloadAttributes.REMAINING_RELOAD);
        if (remainingReload > 0) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void renderReload(final WeaponReloadChangeEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        if (reloadComponent == null) {
            return;
        }
        final int reload = reloadComponent.get(ReloadAttributes.RELOAD);
        final ItemStack item = weapon.getItem();
        final ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof final Damageable damageable)) {
            return;
        }
        if (event.isComplete()) {
            damageable.setDamage(0);
            item.setItemMeta(damageable);
            return;
        }
        final double reloadProgress = (double) (reload - event.getNewReload()) / reload;
        final short maxDurability = item.getType().getMaxDurability();
        final int durability = (int) (reloadProgress * maxDurability);
        damageable.setDamage(maxDurability - durability);
        item.setItemMeta(damageable);
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        if (reloadComponent == null) {
            return;
        }
        final int reload = reloadComponent.get(ReloadAttributes.RELOAD);
        event.addLore(LorePart.RELOAD, List.of(
                new LoreEntry("Reload", "%.1fs".formatted(reload / 20.0))
        ));
    }

    @EventHandler
    private void tickReload(final ServerTickEndEvent event) {
        for (final Weapon weapon : ZombiesPlugin.INSTANCE.getWeapons()) {
            final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
            if (reloadComponent == null) {
                continue;
            }
            final int remainingReload = reloadComponent.get(ReloadAttributes.REMAINING_RELOAD);
            if (remainingReload == 0) {
                continue;
            }
            reloadComponent.set(ReloadAttributes.REMAINING_RELOAD, remainingReload - 1);
            Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(weapon, remainingReload, remainingReload - 1));
        }
    }

    @EventHandler
    private void triggerReloadAuto(final WeaponClipChangeEvent event) {
        if (event.getNewClip() != 0) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        if (reloadComponent == null) {
            return;
        }
        final int reload = reloadComponent.get(ReloadAttributes.RELOAD);
        final int remainingReload = reloadComponent.get(ReloadAttributes.REMAINING_RELOAD);
        reloadComponent.set(ReloadAttributes.REMAINING_RELOAD, reload);
        Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(event.getWeapon(), remainingReload, reload));
    }

    @EventHandler
    private void triggerReloadManual(final PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }
        if (!ZombiesPlayer.isZombiesPlayer(event.getPlayer())) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final Weapon weapon = player.getHeldWeapon();
        if (weapon == null) {
            return;
        }
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        final Component ammoComponent = weapon.getComponent(WeaponComponents.AMMO);
        if (reloadComponent == null || ammoComponent == null) {
            return;
        }
        final int reload = reloadComponent.get(ReloadAttributes.RELOAD);
        final int remainingReload = reloadComponent.get(ReloadAttributes.REMAINING_RELOAD);
        final int maxClip = ammoComponent.get(AmmoAttributes.MAX_CLIP);
        final int clip = ammoComponent.get(AmmoAttributes.CLIP);
        if (remainingReload != 0 || clip == maxClip) {
            return;
        }
        reloadComponent.set(ReloadAttributes.REMAINING_RELOAD, reload);
        Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(weapon, remainingReload, reload));
    }
}
