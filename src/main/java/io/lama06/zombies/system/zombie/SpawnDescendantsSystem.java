package io.lama06.zombies.system.zombie;

import io.lama06.zombies.zombie.DescendantsData;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public final class SpawnDescendantsSystem implements Listener {
    @EventHandler
    private void onEntityDeath(final EntityDeathEvent event) {
        final Zombie zombie = new Zombie(event.getEntity());
        if (!zombie.isZombie()) {
            return;
        }
        final DescendantsData descendantsData = zombie.getData().descendants;
        if (descendantsData == null) {
            return;
        }
        for (int i = 0; i < descendantsData.count(); i++) {
            zombie.getWorld().spawnZombie(zombie.getEntity().getLocation(), descendantsData.type());
        }
    }
}
