package io.lama06.zombies.weapon.ammo;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponAttributes;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.reload.WeaponReloadChangeEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.LoreRenderSystem;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.shoot.WeaponShootEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class AmmoSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final AmmoData data = event.getData().ammo();
        if (data == null) {
            return;
        }
        final ItemStack item = event.getItem();
        item.setAmount(data.clip());
        final PersistentDataContainer pdc = event.getPdc();
        final PersistentDataContainer ammoContainer = pdc.getAdapterContext().newPersistentDataContainer();
        ammoContainer.set(AmmoAttributes.MAX_AMMO.getKey(), PersistentDataType.INTEGER, data.ammo());
        ammoContainer.set(AmmoAttributes.AMMO.getKey(), PersistentDataType.INTEGER, data.ammo());
        ammoContainer.set(AmmoAttributes.MAX_CLIP.getKey(), PersistentDataType.INTEGER, data.clip());
        ammoContainer.set(AmmoAttributes.CLIP.getKey(), PersistentDataType.INTEGER, data.clip());
        pdc.set(WeaponAttributes.AMMO.getKey(), PersistentDataType.TAG_CONTAINER, ammoContainer);
    }

    @EventHandler
    private void renderAmmoClip(final WeaponClipChangeEvent event) {
        final ItemStack item = event.getWeapon().getItem();
        item.setAmount(Math.max(1, event.getNewClip()));
    }

    @EventHandler
    private void triggerLoreRender(final WeaponAmmoChangeEvent event) {
        LoreRenderSystem.renderLore(event.getWeapon());
    }

    @EventHandler
    private void triggerLoreRender(final WeaponClipChangeEvent event) {
        LoreRenderSystem.renderLore(event.getWeapon());
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final PersistentDataContainer pdc = event.getWeapon().getItem().getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer container = pdc.get(WeaponAttributes.AMMO.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final Integer maxAmmo = container.get(AmmoAttributes.MAX_AMMO.getKey(), PersistentDataType.INTEGER);
        final Integer ammo = container.get(AmmoAttributes.AMMO.getKey(), PersistentDataType.INTEGER);
        final Integer maxClip = container.get(AmmoAttributes.MAX_CLIP.getKey(), PersistentDataType.INTEGER);
        final Integer clip = container.get(AmmoAttributes.CLIP.getKey(), PersistentDataType.INTEGER);
        if (maxAmmo == null || ammo == null || maxClip == null || clip == null) {
            return;
        }
        event.addLore(LorePart.AMMO, List.of(
                new LoreEntry("Ammo", ammo + " / " + maxAmmo),
                new LoreEntry("Clip", clip + " / " + maxClip)
        ));
    }

    @EventHandler
    private void reloadAmmo(final WeaponReloadChangeEvent event) {
        if (!event.isComplete()) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final ItemStack item = weapon.getItem();
        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        final PersistentDataContainer ammoContainer = pdc.get(WeaponAttributes.AMMO.getKey(), PersistentDataType.TAG_CONTAINER);
        if (ammoContainer == null) {
            return;
        }
        final Integer maxClip = ammoContainer.get(AmmoAttributes.MAX_CLIP.getKey(), PersistentDataType.INTEGER);
        final Integer clip = ammoContainer.get(AmmoAttributes.CLIP.getKey(), PersistentDataType.INTEGER);
        final Integer ammo = ammoContainer.get(AmmoAttributes.AMMO.getKey(), PersistentDataType.INTEGER);
        if (maxClip == null || clip == null || ammo == null) {
            return;
        }
        final int missingFromClip = maxClip - clip;
        final int addToClip = Math.min(missingFromClip, maxClip);
        final int newClip = clip + addToClip;
        final int newAmmo = ammo - addToClip;
        ammoContainer.set(AmmoAttributes.CLIP.getKey(), PersistentDataType.INTEGER, newClip);
        ammoContainer.set(AmmoAttributes.AMMO.getKey(), PersistentDataType.INTEGER, newAmmo);
        pdc.set(WeaponAttributes.AMMO.getKey(), PersistentDataType.TAG_CONTAINER, ammoContainer);
        item.setItemMeta(meta);
        Bukkit.getPluginManager().callEvent(new WeaponClipChangeEvent(weapon, clip, newClip));
        Bukkit.getPluginManager().callEvent(new WeaponAmmoChangeEvent(weapon, ammo, newAmmo));
    }

    @EventHandler
    private void renderTotalAmmo(final PlayerItemHeldEvent event) {
        renderTotalAmmo(new Weapon(event.getPlayer(), event.getNewSlot()));
    }

    @EventHandler
    private void renderTotalAmmo(final WeaponClipChangeEvent event) {
        renderTotalAmmo(new Weapon(event.getPlayer(), event.getPlayer().getInventory().getHeldItemSlot()));
    }

    @EventHandler
    private void renderTotalAmmo(final WeaponAmmoChangeEvent event) {
        renderTotalAmmo(new Weapon(event.getPlayer(), event.getPlayer().getInventory().getHeldItemSlot()));
    }

    private void renderTotalAmmo(final Weapon weapon) {
        final ItemStack item = weapon.getItem();
        final Player player = weapon.owner();
        if (item == null) {
            player.setLevel(0);
            return;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer ammoContainer = pdc.get(WeaponAttributes.AMMO.getKey(), PersistentDataType.TAG_CONTAINER);
        if (ammoContainer == null) {
            player.setLevel(0);
            return;
        }
        final Integer ammo = ammoContainer.get(AmmoAttributes.AMMO.getKey(), PersistentDataType.INTEGER);
        final Integer clip = ammoContainer.get(AmmoAttributes.CLIP.getKey(), PersistentDataType.INTEGER);
        if (ammo == null || clip == null) {
            player.setLevel(0);
            return;
        }
        final int totalAmmo = ammo + clip;
        player.setLevel(totalAmmo);
    }

    @EventHandler(ignoreCancelled = true)
    private void preventShotWithoutAmmo(final WeaponShootEvent event) {
        final ItemStack item = event.getWeapon().getItem();
        if (item == null) {
            return;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer ammoContainer = pdc.get(WeaponAttributes.AMMO.getKey(), PersistentDataType.TAG_CONTAINER);
        if (ammoContainer == null) {
            return;
        }
        final Integer clip = ammoContainer.get(AmmoAttributes.CLIP.getKey(), PersistentDataType.INTEGER);
        if (clip == null) {
            return;
        }
        if (clip == 0) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void decrementAmmoAfterShot(final WeaponShootEvent event) {
        final Weapon weapon = event.getWeapon();
        final ItemStack item = weapon.getItem();
        if (item == null) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        final PersistentDataContainer ammoContainer = pdc.get(WeaponAttributes.AMMO.getKey(), PersistentDataType.TAG_CONTAINER);
        if (ammoContainer == null) {
            return;
        }
        final Integer clip = ammoContainer.get(AmmoAttributes.CLIP.getKey(), PersistentDataType.INTEGER);
        if (clip == null || clip == 0) {
            return;
        }
        ammoContainer.set(AmmoAttributes.CLIP.getKey(), PersistentDataType.INTEGER, clip - 1);
        pdc.set(WeaponAttributes.AMMO.getKey(), PersistentDataType.TAG_CONTAINER, ammoContainer);
        item.setItemMeta(meta);
        Bukkit.getPluginManager().callEvent(new WeaponClipChangeEvent(weapon, clip, clip - 1));
    }
}
