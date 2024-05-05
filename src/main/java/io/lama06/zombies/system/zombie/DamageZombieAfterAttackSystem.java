package io.lama06.zombies.system.zombie;

import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class DamageZombieAfterAttackSystem implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onAttack(final PlayerAttackZombieEvent event) {
        final Zombie zombie = event.getZombie();
        final Entity entity = zombie.getEntity();
        if (event.isFire()) {
            entity.setFireTicks(5 * 20);
        }
        if (!(entity instanceof final LivingEntity living)) {
            return;
        }
        if (event.isKill()) {
            living.setKiller(event.getPlayer().getBukkit());
            living.setHealth(0);
            return;
        }
        living.setNoDamageTicks(0);
        living.damage(event.getDamage(), event.getPlayer().getBukkit());
        if (event.isFreeze()) {
            living.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 7 * 20, 2));
        }
    }
}
