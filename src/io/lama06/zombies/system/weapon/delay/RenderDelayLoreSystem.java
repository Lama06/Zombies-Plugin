package io.lama06.zombies.system.weapon.delay;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.DelayAttributes;
import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RenderDelayLoreSystem implements Listener {
    @EventHandler
    private void onWeaponRenderLore(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.getComponent(WeaponComponents.DELAY);
        if (delayComponent == null) {
            return;
        }
        final int delay = delayComponent.get(DelayAttributes.DELAY);
        event.addLore(WeaponLoreRenderEvent.Part.DELAY, List.of(
                new WeaponLoreRenderEvent.Entry("Delay", "%.1f".formatted(delay / 20.0))
        ));
    }
}
