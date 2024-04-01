package io.lama06.zombies.system.zombie.laser_attack;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.zombie.LaserAttackAttributes;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public final class MoveLaserAttackGuardianSystem implements Listener {
    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final Zombie zombie : ZombiesPlugin.INSTANCE.getZombies()) {
            final Component laserAttackComponent = zombie.getComponent(ZombieComponents.LASER_ATTACK);
            if (laserAttackComponent == null) {
                continue;
            }
            final UUID guardianUuid = laserAttackComponent.get(LaserAttackAttributes.GUARDIAN);
            final ZombiesWorld world = zombie.getWorld();
            final Entity guardianEntity = world.getBukkit().getEntity(guardianUuid);
            if (!(guardianEntity instanceof final Guardian guardian)) {
                continue;
            }
            final Location zombieHead = zombie.getEntity() instanceof final LivingEntity living
                    ? living.getEyeLocation()
                    : zombie.getEntity().getLocation();
            guardian.teleport(zombieHead);
        }
    }
}
