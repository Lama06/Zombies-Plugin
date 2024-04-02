package io.lama06.zombies.system.zombie.fireball_attack;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.FireBallAttackData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
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
            final Component fireBallAttackComponent = zombie.getComponent(ZombieComponents.FIRE_BALL_ATTACK);
            if (fireBallAttackComponent == null) {
                continue;
            }
            final int delay = fireBallAttackComponent.get(FireBallAttackData.DELAY);
            if (event.getTickNumber() % delay != 0) {
                continue;
            }
            if (!(zombie.getEntity() instanceof final Mob mob)) {
                continue;
            }
            final LivingEntity target = mob.getTarget();
            if (target == null) {
                continue;
            }
            final Vector direction = target.getLocation().clone().subtract(zombie.getEntity().getLocation()).toVector().normalize();
            final World bukkitWorld = zombie.getWorld().getBukkit();
            final Fireball fireball = bukkitWorld.spawn(zombie.getEntity().getLocation().clone().add(direction), Fireball.class);
            fireball.setDirection(direction);
            fireball.setShooter(mob);
        }
    }
}
