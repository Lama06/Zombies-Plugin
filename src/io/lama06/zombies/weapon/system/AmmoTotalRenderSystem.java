package io.lama06.zombies.weapon.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.event.WeaponAmmoChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;

public final class AmmoTotalRenderSystem extends System {
    public AmmoTotalRenderSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onPlayerHoldsItem(final PlayerItemHeldEvent event) {
        if (!game.getPlayers().containsKey(event.getPlayer())) {
            return;
        }
        renderAmmo(game.getPlayers().get(event.getPlayer()), event.getNewSlot());
    }

    @EventHandler
    private void onAmmoChange(final WeaponAmmoChangeEvent event) {
        if (!game.getPlayers().containsValue(event.getPlayer())) {
            return;
        }
        renderAmmo(event.getPlayer(), event.getPlayer().getBukkit().getInventory().getHeldItemSlot());
    }

    private void renderAmmo(final ZombiesPlayer player, final int currentSlot) {
        if (currentSlot >= player.getWeapons().size()) {
            player.getBukkit().setLevel(0);
            return;
        }
        final Weapon weapon = player.getWeapons().get(currentSlot);
        if (weapon.getAmmo() == null) {
            player.getBukkit().setLevel(0);
            return;
        }
        final int totalAmmo = weapon.getAmmo().getAmmo() + weapon.getAmmo().getClip();
        player.getBukkit().setLevel(totalAmmo);
    }
}
