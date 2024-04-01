package io.lama06.zombies.system.zombie.laser_attack;

import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.util.json.UUIDTypeAdapter;
import io.lama06.zombies.zombie.LaserAttackAttributes;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieComponents;
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
        final Zombie zombie = new Zombie(zombieEntity);
        final Component laserAttackComponent = zombie.getComponent(ZombieComponents.LASER_ATTACK);
        if (laserAttackComponent == null) {
            return;
        }
        final double damage = laserAttackComponent.get(LaserAttackAttributes.DAMAGE);
        event.setDamage(damage);
    }
}
