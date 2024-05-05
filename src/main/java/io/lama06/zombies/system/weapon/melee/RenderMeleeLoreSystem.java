package io.lama06.zombies.system.weapon.melee;

import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.MeleeData;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RenderMeleeLoreSystem implements Listener {
    @EventHandler
    private void onWeaponRenderLore(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final MeleeData meleeData = weapon.getData().melee;
        if (meleeData == null) {
            return;
        }
        event.addLore(WeaponLoreRenderEvent.Part.MELEE, List.of(
                new WeaponLoreRenderEvent.Entry("Range", "%.1f".formatted(meleeData.range()))
        ));
    }
}
