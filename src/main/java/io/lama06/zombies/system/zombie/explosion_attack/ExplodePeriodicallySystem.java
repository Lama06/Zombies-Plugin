package io.lama06.zombies.system.zombie.explosion_attack;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.zombie.ExplosionAttackData;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class ExplodePeriodicallySystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        final List<Zombie> zombies = ZombiesPlugin.INSTANCE.getZombies();
        for (final Zombie zombie : zombies) {
            final ExplosionAttackData explosionAttackData = zombie.getData().explosionAttack;
            if (explosionAttackData == null) {
                continue;
            }
            final int delay = explosionAttackData.delay();
            if (delay == 0) {
                continue;
            }
            if (event.getTickNumber() % delay != 0) {
                continue;
            }
            ZombieExplosion.explode(zombie, explosionAttackData);
        }
    }
}
