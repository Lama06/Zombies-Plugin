package io.lama06.zombies.system.zombie.laser_attack;

import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.util.json.UUIDTypeAdapter;
import io.lama06.zombies.zombie.LaserAttackAttributes;
import io.lama06.zombies.zombie.LaserAttackData;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Guardian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;

public final class InitLaserAttackSystem implements Listener {
    @EventHandler
    private void onZombieSpawn(final ZombieSpawnEvent event) {
        final Zombie zombie = event.getZombie();
        final ZombiesWorld world = zombie.getWorld();
        final LaserAttackData laserAttack = event.getData().laserAttack;
        if (laserAttack == null) {
            return;
        }
        final Component laserAttackComponent = zombie.addComponent(ZombieComponents.LASER_ATTACK);
        laserAttackComponent.set(LaserAttackAttributes.DAMAGE, laserAttack.damage());
        final Guardian guardian = world.getBukkit().spawn(zombie.getEntity().getLocation(), Guardian.class);
        guardian.setSilent(true);
        guardian.setInvulnerable(true);
        guardian.setInvisible(true);
        final PersistentDataContainer pdc = guardian.getPersistentDataContainer();
        pdc.set(
                new NamespacedKey(ZombiesPlugin.INSTANCE, LaserAttack.GUARDIAN_ZOMBIE_KEY),
                UUIDTypeAdapter.INSTANCE,
                zombie.getEntity().getUniqueId()
        );
        laserAttackComponent.set(LaserAttackAttributes.GUARDIAN, guardian.getUniqueId());
    }
}
