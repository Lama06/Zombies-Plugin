package io.lama06.zombies.system.perk;

import io.lama06.zombies.GlobalPerk;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.perk.PlayerPickupPerkItemEvent;
import io.lama06.zombies.event.weapon.WeaponAmmoChangeEvent;
import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.AmmoData;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PerformMaxAmmoPerkSystem implements Listener {
    @EventHandler
    private void onPlayerPickupPerk(final PlayerPickupPerkItemEvent event) {
        final ZombiesPlayer player = event.getPlayer();
        final ZombiesWorld world = player.getWorld();
        final GlobalPerk perk = event.getPerk();
        if (perk != GlobalPerk.MAX_AMMO) {
            return;
        }
        for (final ZombiesPlayer otherPlayer : world.getAlivePlayers()) {
            for (final Weapon weapon : otherPlayer.getWeapons()) {
                final AmmoData ammoData = weapon.getData().ammo;
                if (ammoData == null) {
                    continue;
                }
                final Component ammoComponent = weapon.getComponent(WeaponComponents.AMMO);
                if (ammoComponent == null) {
                    continue;
                }
                final int oldClip = ammoComponent.get(AmmoData.CLIP);
                final int oldAmmo = ammoComponent.get(AmmoData.AMMO);
                ammoComponent.set(AmmoData.CLIP, ammoData.clip());
                ammoComponent.set(AmmoData.AMMO, ammoData.ammo());
                Bukkit.getPluginManager().callEvent(new WeaponClipChangeEvent(weapon, oldClip, ammoData.clip()));
                Bukkit.getPluginManager().callEvent(new WeaponAmmoChangeEvent(weapon, oldAmmo, ammoData.ammo()));
            }
        }
    }
}
