package io.lama06.zombies.system.weapon.attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.AttackData;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitAttackSystem implements Listener {
    @EventHandler
    private void onWeaponCreate(final WeaponCreateEvent event) {
        final AttackData data = event.getData().attack;
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component attackComponent = weapon.addComponent(WeaponComponents.ATTACK);
        attackComponent.set(AttackData.DAMAGE, data.damage());
        attackComponent.set(AttackData.FIRE, data.fire());
        attackComponent.set(AttackData.GOLD, data.gold());
    }
}
