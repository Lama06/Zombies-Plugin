package io.lama06.zombies.system.zombie.laser_attack;

import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.util.json.UUIDTypeAdapter;
import io.lama06.zombies.zombie.LaserAttackData;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

public final class LaserAttackDamageSystem implements Listener {
    @EventHandler
    private void onEntityDamage(final EntityDamageByEntityEvent event) {
        final Entity damager = event.getDamager();
        if (!(damager instanceof final Guardian guardian)) {
            return;
        }
        final PersistentDataContainer pdc = guardian.getPersistentDataContainer();
        final UUID zombieUuid = pdc.get(
                new NamespacedKey(ZombiesPlugin.INSTANCE, LaserAttack.GUARDIAN_ZOMBIE_KEY),
                UUIDTypeAdapter.INSTANCE
        );
        if (zombieUuid == null) {
            return;
        }
        final Entity zombieEntity = damager.getWorld().getEntity(zombieUuid);
        if (zombieEntity == null) {
            return;
        }
        final Zombie zombie = new Zombie(zombieEntity);
        if (!zombie.isZombie()) {
            return;
        }
        final LaserAttackData laserAttackData = zombie.getData().laserAttack;
        if (laserAttackData == null) {
            return;
        }
        event.setDamage(laserAttackData.damage());
    }
}
