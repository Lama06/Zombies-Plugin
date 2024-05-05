package io.lama06.zombies.system.weapon.shoot;

import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.ShootData;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RenderShootLoreSystem implements Listener {
    @EventHandler
    private void onWeaponLoreRender(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final ShootData shootData = weapon.getData().shoot;
        if (shootData == null) {
            return;
        }
        event.addLore(WeaponLoreRenderEvent.Part.SHOOT, List.of(
                new WeaponLoreRenderEvent.Entry("Bullets", Integer.toString(shootData.bullets())),
                new WeaponLoreRenderEvent.Entry("Precision", Math.round(shootData.precision() * 100) + "%")
        ));
    }
}
