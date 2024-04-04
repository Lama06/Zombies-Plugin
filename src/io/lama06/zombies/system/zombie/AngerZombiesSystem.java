package io.lama06.zombies.system.zombie;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.DyeColor;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Comparator;

public final class AngerZombiesSystem implements Listener {
    @EventHandler
    private void onZombieSpawn(final ZombieSpawnEvent event) {
        final Zombie zombie = event.getZombie();
        final ZombiesPlayer nearestPlayer = getNearestPlayer(zombie);
        if (nearestPlayer == null) {
            return;
        }
        if (zombie.getEntity() instanceof final PigZombie pigZombie) {
            angerPigZombie(pigZombie, nearestPlayer);
        } else if (zombie.getEntity() instanceof final Wolf wolf) {
            angerWolf(wolf, nearestPlayer);
        }
    }

    private void angerPigZombie(final PigZombie zombie, final ZombiesPlayer nearestPlayer) {
        zombie.setAngry(true);
        zombie.setTarget(nearestPlayer.getBukkit());
    }

    private void angerWolf(final Wolf wolf, final ZombiesPlayer nearestPlayer) {
        wolf.setAngry(true);
        wolf.setTarget(nearestPlayer.getBukkit());
        wolf.setCollarColor(DyeColor.RED);
    }

    private ZombiesPlayer getNearestPlayer(final Zombie zombie) {
        return zombie.getWorld().getAlivePlayers().stream()
                .min(Comparator.comparingDouble(player -> player.getBukkit().getLocation().distance(zombie.getEntity().getLocation())))
                .orElse(null);
    }
}
