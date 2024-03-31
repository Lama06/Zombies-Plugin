package io.lama06.zombies.system.weapon.reload;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.AmmoAttributes;
import io.lama06.zombies.weapon.ReloadAttributes;
import io.lama06.zombies.event.weapon.WeaponReloadChangeEvent;
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
