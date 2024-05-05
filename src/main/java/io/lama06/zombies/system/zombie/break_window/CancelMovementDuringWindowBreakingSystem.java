package io.lama06.zombies.system.zombie.break_window;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.BreakWindowData;
import io.lama06.zombies.zombie.Zombie;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class CancelMovementDuringWindowBreakingSystem implements Listener {
    @EventHandler(ignoreCancelled = true)
    private void onEntityMove(final EntityMoveEvent event) {
        final Entity entity = event.getEntity();
        final Zombie zombie = new Zombie(entity);
        if (!zombie.isZombie()) {
            return;
        }
        final Component breakWindowComponent = zombie.getComponent(Zombie.BREAK_WINDOW);
        if (breakWindowComponent == null) {
            return;
        }
        final Integer remainingTime = breakWindowComponent.get(BreakWindowData.REMAINING_TIME);
        if (remainingTime == null) {
            return;
        }
        event.setCancelled(true);
    }
}
