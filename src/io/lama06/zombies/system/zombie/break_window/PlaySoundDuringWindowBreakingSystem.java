package io.lama06.zombies.system.zombie.break_window;

import io.lama06.zombies.event.zombie.BreakWindowTickEvent;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class PlaySoundDuringWindowBreakingSystem implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onBreakWindowTick(final BreakWindowTickEvent event) {
        final int SOUND_DELAY = 2 * 20;
        final Zombie zombie = event.getZombie();
        if (event.getNewRemainingTime() % SOUND_DELAY != 0) {
            return;
        }
        zombie.getWorld().getBukkit().playSound(
                zombie.getEntity(),
                Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR,
                SoundCategory.HOSTILE,
                1,
                1
        );
    }
}
