package io.lama06.zombies.system.zombie.fireball_attack;

import io.lama06.zombies.zombie.FireBallAttackData;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public final class DamagePlayerWhenFireballExplodesSystem implements Listener {
    @EventHandler
    private void onEntityDamage(final EntityDamageByEntityEvent event) {
        final Entity damager = event.getDamager();
        if (!(damager instanceof final Fireball fireball)) {
            return;
        }
        final Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        final ProjectileSource shooter = fireball.getShooter();
        if (!(shooter instanceof final LivingEntity livingShooter)) {
            return;
        }
        final Zombie zombieShooter = new Zombie(livingShooter);
        if (!zombieShooter.isZombie()) {
            return;
        }
        final FireBallAttackData fireBallAttackData = zombieShooter.getData().fireBallAttack;
        if (fireBallAttackData == null) {
            return;
        }
        event.setDamage(fireBallAttackData.damage());
    }
}
