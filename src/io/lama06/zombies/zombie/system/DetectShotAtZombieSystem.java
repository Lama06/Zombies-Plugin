package io.lama06.zombies.zombie.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.weapon.event.WeaponShootEvent;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.event.PlayerAttacksZombieEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.util.RayTraceResult;

public final class DetectShotAtZombieSystem extends System {
    public DetectShotAtZombieSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onWeaponShoot(final WeaponShootEvent event) {
        if (!event.getGame().equals(game)) {
            return;
        }
        for (final WeaponShootEvent.Bullet bullet : event.getBullets()) {
            final RayTraceResult ray = event.getPlayer().getBukkit().rayTraceEntities(100);
            if (ray == null || ray.getHitEntity() == null) {
                return;
            }
            if (!game.getZombies().containsKey(ray.getHitEntity())) {
                return;
            }
            final Zombie zombie = game.getZombies().get(ray.getHitEntity());
            if (zombie.getHealth() != null) {
                zombie.getHealth().damage((int) event.getWeapon().getShoot().getDamage());
                Bukkit.broadcastMessage(zombie.getHealth().getHealth() + "");
            }
            Bukkit.getPluginManager().callEvent(new PlayerAttacksZombieEvent(zombie, event.getPlayer(), event.getWeapon()));
        }
    }
}
