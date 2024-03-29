package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.SpawnRate;
import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import org.bukkit.event.EventHandler;

public final class StartNextRoundSystem extends System {
    public StartNextRoundSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        if (!game.getRemainingZombies().isEmpty()) {
            return;
        }
        if (!game.getZombies().isEmpty()) {
            return;
        }
        game.setRound(game.getRound()+1);
        game.setRemainingZombies(SpawnRate.SPAWN_RATES.get(game.getRound()-1).zombies());
    }
}
