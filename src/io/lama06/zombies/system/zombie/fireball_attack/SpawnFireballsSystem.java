package io.lama06.zombies.system.zombie.fireball_attack;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.zombie.FireBallAttackData;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public final class SpawnFireballsSystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        for (final Zombie zombie : ZombiesPlugin.INSTANCE.getZombies()) {
            final FireBallAttackData fireBallAttackData = zombie.getData().fireBallAttack;
            if (fireBallAttackData == null) {
                continue;
            }
            if (event.getTickNumber() % fireBallAttackData.delay() != 0) {
                continue;
            }

            if (!(zombie.getEntity() instanceof final Mob mob)) {
                continue;
            }
            final LivingEntity target = mob.getTarget();
            if (target == null) {
                continue;
            }
            final Vector directionPreNormalization = target.getLocation().clone().subtract(zombie.getEntity().getLocation()).toVector();
            if (directionPreNormalization.isZero()) {
                // Otherwise, we'd try to normalize the zero vector, which gives (NaN, NaN, NaN)
                continue;
            }
            final Vector direction = directionPreNormalization.normalize();
            final World bukkitWorld = zombie.getWorld().getBukkit();
            final Fireball fireball = bukkitWorld.spawn(zombie.getEntity().getLocation().clone().add(direction), Fireball.class);
            fireball.setDirection(direction);
            fireball.setShooter(mob);
        }
    }
}
