package io.lama06.zombies.system.zombie.break_window;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.zombie.BreakWindowTickEvent;
import io.lama06.zombies.zombie.BreakWindowAttributes;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class TickWindowBreakingSystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        for (final Zombie zombie : ZombiesPlugin.INSTANCE.getZombies()) {
            tickZombie(zombie);
        }
    }

    private void tickZombie(final Zombie zombie) {
        final Component breakWindowComponent = zombie.getComponent(ZombieComponents.BREAK_WINDOW);
        if (breakWindowComponent == null) {
            return;
        }
        final Integer remainingTime = breakWindowComponent.getOrDefault(BreakWindowAttributes.REMAINING_TIME, null);
        final BlockPosition block = breakWindowComponent.getOrDefault(BreakWindowAttributes.BLOCK, null);
        if (remainingTime == null || block == null) {
            return;
        }
        if (remainingTime == 1) {
            breakWindowComponent.remove(BreakWindowAttributes.REMAINING_TIME);
            breakWindowComponent.remove(BreakWindowAttributes.BLOCK);
            block.toLocation(zombie.getWorld().getBukkit()).getBlock().setType(Material.AIR);
            return;
        }
        breakWindowComponent.set(BreakWindowAttributes.REMAINING_TIME, remainingTime - 1);
        if (!new BreakWindowTickEvent(zombie, remainingTime - 1).callEvent()) {
            breakWindowComponent.remove(BreakWindowAttributes.REMAINING_TIME);
            breakWindowComponent.remove(BreakWindowAttributes.BLOCK);
        }
    }
}
