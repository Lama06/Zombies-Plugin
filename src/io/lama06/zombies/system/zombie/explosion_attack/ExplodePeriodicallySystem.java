package io.lama06.zombies.system.zombie.explosion_attack;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.ExplosionAttackData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class ExplodePeriodicallySystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        final List<Zombie> zombies = ZombiesPlugin.INSTANCE.getZombies();
        for (final Zombie zombie : zombies) {
            final Component explosionAttackComponent = zombie.getComponent(ZombieComponents.EXPLOSION_ATTACK);
            if (explosionAttackComponent == null) {
                continue;
            }
            final int delay = explosionAttackComponent.getOrDefault(ExplosionAttackData.DELAY, 0);
            if (delay == 0) {
                continue;
            }
            if (event.getTickNumber() % delay != 0) {
                continue;
            }
            ZombieExplosion.explode(zombie, explosionAttackComponent);
        }
    }
}
