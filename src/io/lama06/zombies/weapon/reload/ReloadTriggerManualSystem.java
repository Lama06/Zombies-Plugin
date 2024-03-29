package io.lama06.zombies.weapon.reload;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public final class ReloadTriggerManualSystem extends System {
    public ReloadTriggerManualSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }
        if (!game.getPlayers().containsKey(event.getPlayer())) {
            return;
        }
        final ZombiesPlayer player = game.getPlayers().get(event.getPlayer());
        final Weapon weapon = player.getHeldWeapon();
        if (weapon == null || weapon.getReload() == null) {
            return;
        }
        if (weapon.getAmmo() != null && weapon.getAmmo().getClip() == weapon.getAmmo().getMaxClip()) {
            return;
        }
        weapon.getReload().startReload();
    }
}
