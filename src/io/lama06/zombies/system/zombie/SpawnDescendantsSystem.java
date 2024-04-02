package io.lama06.zombies.system.zombie;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.zombie.DescendantsData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import io.lama06.zombies.zombie.ZombieType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public final class SpawnDescendantsSystem implements Listener {
    @EventHandler
    private void onZombieSpawn(final ZombieSpawnEvent event) {
        final DescendantsData descendants = event.getData().descendants;
        if (descendants == null) {
            return;
        }
        final Zombie zombie = event.getZombie();
        final Component descendantsComponent = zombie.addComponent(ZombieComponents.DESCENDANTS);
        descendantsComponent.set(DescendantsData.COUNT, descendants.count());
        descendantsComponent.set(DescendantsData.TYPE, descendants.type());
    }

    @EventHandler
    private void onEntityDeath(final EntityDeathEvent event) {
        final Zombie zombie = new Zombie(event.getEntity());
        if (!zombie.isZombie()) {
            return;
        }
        final Component descendantsComponent = zombie.getComponent(ZombieComponents.DESCENDANTS);
        if (descendantsComponent == null) {
            return;
        }
        final int count = descendantsComponent.get(DescendantsData.COUNT);
        final ZombieType type = descendantsComponent.get(DescendantsData.TYPE);
        for (int i = 0; i < count; i++) {
            zombie.getWorld().spawnZombie(zombie.getEntity().getLocation(), type.data);
        }
    }
}
