package io.lama06.zombies.weapon.ammo;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.reload.WeaponReloadChangeEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.LoreRenderSystem;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.shoot.WeaponShootEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class AmmoSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final AmmoData data = event.getData().ammo();
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        weapon.getItem().setAmount(data.clip());
        final Component ammoComponent = weapon.addComponent(WeaponComponents.AMMO);
        ammoComponent.set(AmmoAttributes.MAX_AMMO, data.ammo());
        ammoComponent.set(AmmoAttributes.AMMO, data.ammo());
        ammoComponent.set(AmmoAttributes.MAX_CLIP, data.clip());
        ammoComponent.set(AmmoAttributes.CLIP, data.clip());
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
        final Weapon weapon = event.getWeapon();
        final Component component = weapon.getComponent(WeaponComponents.AMMO);
        if (component == null) {
            return;
        }
        final int maxAmmo = component.get(AmmoAttributes.MAX_AMMO);
        final int ammo = component.get(AmmoAttributes.AMMO);
        final int maxClip = component.get(AmmoAttributes.MAX_CLIP);
        final int clip = component.get(AmmoAttributes.CLIP);
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
        final Component ammoComponent = weapon.getComponent(WeaponComponents.AMMO);
        if (ammoComponent == null) {
            return;
        }
        final int ammo = ammoComponent.get(AmmoAttributes.AMMO);
        final int maxClip = ammoComponent.get(AmmoAttributes.MAX_CLIP);
        final int clip = ammoComponent.get(AmmoAttributes.CLIP);
        final int missingFromClip = maxClip - clip;
        final int addToClip = Math.min(missingFromClip, maxClip);
        final int newClip = clip + addToClip;
        final int newAmmo = ammo - addToClip;
        ammoComponent.set(AmmoAttributes.CLIP, newClip);
        ammoComponent.set(AmmoAttributes.AMMO, newAmmo);
        Bukkit.getPluginManager().callEvent(new WeaponClipChangeEvent(weapon, clip, newClip));
        Bukkit.getPluginManager().callEvent(new WeaponAmmoChangeEvent(weapon, ammo, newAmmo));
    }

    @EventHandler
    private void renderTotalAmmo(final PlayerItemHeldEvent event) {
        if (!ZombiesPlayer.isZombiesPlayer(event.getPlayer())) {
            return;
        }
        renderTotalAmmo(new ZombiesPlayer(event.getPlayer()), event.getNewSlot());
    }

    @EventHandler
    private void renderTotalAmmo(final WeaponClipChangeEvent event) {
        renderTotalAmmo(event.getPlayer());
    }

    @EventHandler
    private void renderTotalAmmo(final WeaponAmmoChangeEvent event) {
        renderTotalAmmo(event.getPlayer());
    }

    private void renderTotalAmmo(final ZombiesPlayer player) {
        renderTotalAmmo(player, player.getBukkit().getInventory().getHeldItemSlot());
    }

    private void renderTotalAmmo(final ZombiesPlayer player, final int slot) {
        final Weapon weapon = player.getWeapon(slot);
        if (weapon == null) {
            player.getBukkit().setLevel(0);
            return;
        }
        final Component ammoComponent = weapon.getComponent(WeaponComponents.AMMO);
        if (ammoComponent == null) {
            player.getBukkit().setLevel(0);
            return;
        }
        final int ammo = ammoComponent.get(AmmoAttributes.AMMO);
        final int clip = ammoComponent.get(AmmoAttributes.CLIP);
        final int totalAmmo = ammo + clip;
        player.getBukkit().setLevel(totalAmmo);
    }

    @EventHandler(ignoreCancelled = true)
    private void preventShotWithoutAmmo(final WeaponShootEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component ammoComponent = weapon.getComponent(WeaponComponents.AMMO);
        if (ammoComponent == null) {
            return;
        }
        final int clip = ammoComponent.get(AmmoAttributes.CLIP);
        if (clip == 0) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void decrementAmmoAfterShot(final WeaponShootEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component ammoComponent = weapon.getComponent(WeaponComponents.AMMO);
        if (ammoComponent == null) {
            return;
        }
        final int clip = ammoComponent.get(AmmoAttributes.CLIP);
        if (clip == 0) {
            return;
        }
        ammoComponent.set(AmmoAttributes.CLIP, clip - 1);
        Bukkit.getPluginManager().callEvent(new WeaponClipChangeEvent(weapon, clip, clip - 1));
    }
}
