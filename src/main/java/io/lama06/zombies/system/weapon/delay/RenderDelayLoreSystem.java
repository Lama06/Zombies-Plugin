package io.lama06.zombies.system.weapon.delay;

import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.DelayData;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RenderDelayLoreSystem implements Listener {
    @EventHandler
    private void onWeaponRenderLore(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final DelayData delayData = weapon.getData().delay;
        if (delayData == null) {
            return;
        }
        event.addLore(WeaponLoreRenderEvent.Part.DELAY, List.of(
                new WeaponLoreRenderEvent.Entry("Delay", "%.1f".formatted(delayData.delay() / 20.0))
        ));
    }
}
