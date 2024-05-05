package io.lama06.zombies.system.weapon.ammo;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.AmmoData;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.event.weapon.WeaponAmmoChangeEvent;
import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public final class RenderTotalAmmoSystem implements Listener {
    @EventHandler
    private void onPlayerItemHeld(final PlayerItemHeldEvent event) {
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final ZombiesWorld world = player.getWorld();
        if (!world.isGameRunning() || !player.isAlive()) {
            return;
        }
        renderTotalAmmo(new ZombiesPlayer(event.getPlayer()), event.getNewSlot());
    }

    @EventHandler
    private void onWeaponClipChange(final WeaponClipChangeEvent event) {
        renderTotalAmmo(event.getPlayer());
    }

    @EventHandler
    private void onWeaponAmmoChange(final WeaponAmmoChangeEvent event) {
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
        final Component ammoComponent = weapon.getComponent(Weapon.AMMO);
        if (ammoComponent == null) {
            player.getBukkit().setLevel(0);
            return;
        }
        final int ammo = ammoComponent.get(AmmoData.AMMO);
        final int clip = ammoComponent.get(AmmoData.CLIP);
        final int totalAmmo = ammo + clip;
        player.getBukkit().setLevel(totalAmmo);
    }
}
