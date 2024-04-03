package io.lama06.zombies.system.weapon.reload;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.weapon.WeaponReloadChangeEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class StartReloadManualSystem implements Listener {
    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        if (!player.getWorld().isGameRunning() || !player.isAlive()) {
            return;
        }
        final Weapon weapon = player.getHeldWeapon();
        if (weapon == null) {
            return;
        }
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        final Component ammoComponent = weapon.getComponent(WeaponComponents.AMMO);
        if (reloadComponent == null || ammoComponent == null) {
            return;
        }
        final int reload = weapon.getData().reload.reload();
        final int remainingReload = reloadComponent.get(ReloadData.REMAINING_RELOAD);
        final int maxClip = weapon.getData().ammo.clip();
        final int clip = ammoComponent.get(AmmoData.CLIP);
        if (remainingReload != 0 || clip == maxClip) {
            return;
        }
        reloadComponent.set(ReloadData.REMAINING_RELOAD, reload);
        Bukkit.getPluginManager().callEvent(new WeaponReloadChangeEvent(weapon, remainingReload, reload));
    }
}
