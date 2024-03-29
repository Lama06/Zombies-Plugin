package io.lama06.zombies.zombie.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.event.PlayerAttacksZombieEvent;
import org.bukkit.event.EventHandler;

public final class ApplyAttackDamageSystem extends System {
    public ApplyAttackDamageSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler(ignoreCancelled = true)
    private void onAttack(final PlayerAttacksZombieEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        if (event.getZombie().getHealth() == null) {
            return;
        }

        event.getZombie().getHealth().damage(event.getDamage());
    }
}
