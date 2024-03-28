package io.lama06.zombies.weapon.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.EventHandler;

public final class DelayTickSystem extends System {
    public DelayTickSystem(final ZombiesGame game) {
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
        if (weapon.getDelay() == null || weapon.getDelay().getRemainingDelay() == 0) {
            return;
        }
        weapon.getDelay().setRemainingDelay(weapon.getDelay().getRemainingDelay() - 1);
    }
}
