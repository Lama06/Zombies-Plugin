package io.lama06.zombies.system.weapon.shoot;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.ShootAttributes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RenderShootLoreSystem implements Listener {
    @EventHandler
    private void onWeaponLoreRender(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component shootComponent = weapon.getComponent(WeaponComponents.SHOOT);
        if (shootComponent == null) {
            return;
        }
        final int bullets = shootComponent.get(ShootAttributes.BULLETS);
        final double precision = shootComponent.get(ShootAttributes.PRECISION);
        event.addLore(WeaponLoreRenderEvent.Part.SHOOT, List.of(
                new WeaponLoreRenderEvent.Entry("Bullets", Integer.toString(bullets)),
                new WeaponLoreRenderEvent.Entry("Precision", Math.round(precision * 100) + "%")
        ));
    }
}
