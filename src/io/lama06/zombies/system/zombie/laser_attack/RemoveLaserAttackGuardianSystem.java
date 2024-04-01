package io.lama06.zombies.system.zombie.laser_attack;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.util.json.UUIDTypeAdapter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;


public final class RemoveLaserAttackGuardianSystem implements Listener {
    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getWorlds()) {
            for (final Guardian guardian : world.getBukkit().getEntitiesByClass(Guardian.class)) {
                final PersistentDataContainer pdc = guardian.getPersistentDataContainer();
                final UUID zombieUuid = pdc.get(
                        new NamespacedKey(ZombiesPlugin.INSTANCE, LaserAttack.GUARDIAN_ZOMBIE_KEY),
                        UUIDTypeAdapter.INSTANCE
                );
                if (zombieUuid == null) {
                    continue;
                }
                final Entity zombie = world.getBukkit().getEntity(zombieUuid);
                if (zombie != null) {
                    return;
                }
                guardian.remove();
            }
        }
    }
}
