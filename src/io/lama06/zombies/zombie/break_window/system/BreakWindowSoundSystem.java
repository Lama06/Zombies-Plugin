package io.lama06.zombies.zombie.break_window.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.break_window.event.BreakWindowTickEvent;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;

public final class BreakWindowSoundSystem extends System {
    public BreakWindowSoundSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler(ignoreCancelled = true)
    private void onTick(final BreakWindowTickEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        if (event.getNewRemainingTime() % (2*20) != 0) {
            return;
        }
        game.getWorld().playSound(event.getZombie().getEntity(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1, 1);
    }
}
