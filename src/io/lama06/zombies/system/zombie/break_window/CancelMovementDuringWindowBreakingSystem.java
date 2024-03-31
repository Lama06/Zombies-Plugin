package io.lama06.zombies.system.zombie.break_window;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.BreakWindowAttributes;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class CancelMovementDuringWindowBreakingSystem implements Listener {
    @EventHandler(ignoreCancelled = true)
    private void onEntityMove(final EntityMoveEvent event) {
        final Entity entity = event.getEntity();
        if (!Zombie.isZombie(entity)) {
            return;
        }
        final Zombie zombie = new Zombie(entity);
        final Component breakWindowComponent = zombie.getComponent(ZombieComponents.BREAK_WINDOW);
        if (breakWindowComponent == null) {
            return;
        }
        final Integer remainingTime = breakWindowComponent.getOrDefault(BreakWindowAttributes.REMAINING_TIME, null);
        if (remainingTime == null) {
            return;
        }
        event.setCancelled(true);
    }
}
