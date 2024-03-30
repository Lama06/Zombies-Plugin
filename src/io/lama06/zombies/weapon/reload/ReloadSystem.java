package io.lama06.zombies.weapon.reload;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponAttributes;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class ReloadSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final Integer reload = event.getData().reload();
        if (reload == null) {
            return;
        }
        final PersistentDataContainer pdc = event.getPdc();
        final PersistentDataContainer reloadContainer = pdc.getAdapterContext().newPersistentDataContainer();
        reloadContainer.set(ReloadAttributes.RELOAD.getKey(), PersistentDataType.INTEGER, reload);
        reloadContainer.set(ReloadAttributes.REMAINING_RELOAD.getKey(), PersistentDataType.INTEGER, 0);
        pdc.set(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER, reloadContainer);
    }

    @EventHandler(ignoreCancelled = true)
    private void preventShootDuringReload(final WeaponShootEvent event) {
        final ItemStack item = event.getWeapon().getItem();
        if (item == null) {
            return;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer reloadContainer = pdc.get(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER);
        if (reloadContainer == null) {
            return;
        }
        final Integer remainingReload = reloadContainer.get(ReloadAttributes.REMAINING_RELOAD.getKey(), PersistentDataType.INTEGER);
        if (remainingReload == null || remainingReload == 0) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void renderReload(final WeaponReloadChangeEvent event) {
        final ItemStack item = event.getWeapon().getItem();
        if (item == null) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        final PersistentDataContainer reloadContainer = pdc.get(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER);
        if (reloadContainer == null) {
            return;
        }
        final Integer reload = reloadContainer.get(ReloadAttributes.RELOAD.getKey(), PersistentDataType.INTEGER);
        final Integer remainingReload = reloadContainer.get(ReloadAttributes.REMAINING_RELOAD.getKey(), PersistentDataType.INTEGER);
        if (reload == null || remainingReload == null) {
            return;
        }
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
        final PersistentDataContainer pdc = event.getWeapon().getItem().getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer container = pdc.get(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final Integer reload = container.get(ReloadAttributes.RELOAD.getKey(), PersistentDataType.INTEGER);
        if (reload == null) {
            return;
        }
        event.addLore(LorePart.RELOAD, List.of(
                new LoreEntry("Reload", "%.1f".formatted(reload / 20.0))
        ));
    }

    @EventHandler
    private void tickReload(final ServerTickEndEvent event) {
        for (final Weapon weapon : Weapon.getAllWeapons()) {
            final ItemStack item = weapon.getItem();
            if (item == null) {
                continue;
            }
            final ItemMeta meta = item.getItemMeta();
            final PersistentDataContainer pdc = meta.getPersistentDataContainer();
            final PersistentDataContainer reloadContainer = pdc.get(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER);
            if (reloadContainer == null) {
                continue;
            }
            final Integer reload = reloadContainer.get(ReloadAttributes.RELOAD.getKey(), PersistentDataType.INTEGER);
            final Integer remainingReload = reloadContainer.get(ReloadAttributes.REMAINING_RELOAD.getKey(), PersistentDataType.INTEGER);
            if (reload == null || remainingReload == null) {
                continue;
            }
            if (remainingReload == 0) {
                continue;
            }
            reloadContainer.set(ReloadAttributes.REMAINING_RELOAD.getKey(), PersistentDataType.INTEGER, remainingReload - 1);
            pdc.set(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER, reloadContainer);
            item.setItemMeta(meta);
            Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(weapon, remainingReload, remainingReload - 1));
        }
    }

    @EventHandler
    private void triggerReloadAuto(final WeaponClipChangeEvent event) {
        if (event.getNewClip() != 0) {
            return;
        }
        final ItemStack item = event.getWeapon().getItem();
        if (item == null) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        final PersistentDataContainer reloadContainer = pdc.get(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER);
        if (reloadContainer == null) {
            return;
        }
        final Integer reload = reloadContainer.get(ReloadAttributes.RELOAD.getKey(), PersistentDataType.INTEGER);
        final Integer remainingReload = reloadContainer.get(ReloadAttributes.REMAINING_RELOAD.getKey(), PersistentDataType.INTEGER);
        if (reload == null || remainingReload == null) {
            return;
        }
        reloadContainer.set(ReloadAttributes.REMAINING_RELOAD.getKey(), PersistentDataType.INTEGER, reload);
        pdc.set(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER, reloadContainer);
        item.setItemMeta(meta);
        Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(event.getWeapon(), remainingReload, reload));
    }

    @EventHandler
    private void triggerReloadAuto(final PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }
        final Weapon weapon = Weapon.getHeldWeapon(event.getPlayer());
        if (weapon == null) {
            return;
        }
        final ItemStack item = weapon.getItem();
        if (item == null) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        final PersistentDataContainer reloadContainer = pdc.get(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER);
        final PersistentDataContainer ammoContainer = pdc.get(WeaponAttributes.AMMO.getKey(), PersistentDataType.TAG_CONTAINER);
        if (reloadContainer == null || ammoContainer == null) {
            return;
        }
        final Integer reload = reloadContainer.get(ReloadAttributes.RELOAD.getKey(), PersistentDataType.INTEGER);
        final Integer remainingReload = reloadContainer.get(ReloadAttributes.REMAINING_RELOAD.getKey(), PersistentDataType.INTEGER);
        final Integer maxClip = ammoContainer.get(AmmoAttributes.MAX_CLIP.getKey(), PersistentDataType.INTEGER);
        final Integer clip = ammoContainer.get(AmmoAttributes.CLIP.getKey(), PersistentDataType.INTEGER);
        if (reload == null || remainingReload == null || maxClip == null || clip == null) {
            return;
        }
        if (clip.equals(maxClip)) {
            return;
        }
        reloadContainer.set(ReloadAttributes.REMAINING_RELOAD.getKey(), PersistentDataType.INTEGER, reload);
        pdc.set(WeaponAttributes.RELOAD.getKey(), PersistentDataType.TAG_CONTAINER, reloadContainer);
        item.setItemMeta(meta);
        Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(weapon, remainingReload, reload));
    }
}
