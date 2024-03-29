package io.lama06.zombies.weapon.reload;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.EventHandler;

public final class ReloadTickSystem extends System {
    public ReloadTickSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final ZombiesPlayer player : game.getPlayers().values()) {
            for (final Weapon weapon : player.getWeapons()) {
                tickWeapon(weapon);
            }
        }
    }

    private void tickWeapon(final Weapon weapon) {
        if (weapon.getReload() == null || weapon.getReload().getRemainingReload() == 0) {
            return;
        }
        weapon.getReload().setRemainingReload(weapon.getReload().getRemainingReload() - 1);
    }
}
