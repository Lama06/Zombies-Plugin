package io.lama06.zombies.system.player;

import io.lama06.zombies.event.player.PlayerKillZombieEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public final class DetectPlayerKillsZombieSystem implements Listener {
    @EventHandler
    private void onEntityDeath(final EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        final Zombie zombie = new Zombie(entity);
        if (!zombie.isZombie()) {
            return;
        }
        if (entity.getKiller() == null) {
            return;
        }
        final ZombiesPlayer killer = new ZombiesPlayer(entity.getKiller());
        Bukkit.getPluginManager().callEvent(new PlayerKillZombieEvent(killer, zombie));
    }
}
