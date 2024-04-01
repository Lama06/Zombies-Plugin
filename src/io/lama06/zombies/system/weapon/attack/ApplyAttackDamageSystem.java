package io.lama06.zombies.system.weapon.attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.AttackAttributes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ApplyAttackDamageSystem implements Listener {
    @EventHandler
    private void onPlayerAttackZombie(final PlayerAttackZombieEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component attackComponent = weapon.getComponent(WeaponComponents.ATTACK);
        if (attackComponent == null) {
            return;
        }
        final boolean fire = attackComponent.get(AttackAttributes.FIRE);
        final double damage = attackComponent.get(AttackAttributes.DAMAGE);
        event.setBaseDamage(damage);
        event.setFire(fire);
    }
}
