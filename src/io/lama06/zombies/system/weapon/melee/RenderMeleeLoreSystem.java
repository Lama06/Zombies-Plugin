package io.lama06.zombies.system.weapon.melee;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.MeleeAttributes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RenderMeleeLoreSystem implements Listener {
    @EventHandler
    private void onWeaponRenderLore(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component meleeComponent = weapon.getComponent(WeaponComponents.MELEE);
        if (meleeComponent == null) {
            return;
        }
        final double range = meleeComponent.get(MeleeAttributes.RANGE);
        event.addLore(WeaponLoreRenderEvent.Part.MELEE, List.of(
                new WeaponLoreRenderEvent.Entry("Range", "%.1f".formatted(range))
        ));
    }
}
