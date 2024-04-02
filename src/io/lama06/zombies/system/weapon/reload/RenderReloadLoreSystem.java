package io.lama06.zombies.system.weapon.reload;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.ReloadData;
import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RenderReloadLoreSystem implements Listener {
    @EventHandler
    private void onWeaponLoreRender(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component reloadComponent = weapon.getComponent(WeaponComponents.RELOAD);
        if (reloadComponent == null) {
            return;
        }
        final int reload = reloadComponent.get(ReloadData.RELOAD);
        event.addLore(WeaponLoreRenderEvent.Part.RELOAD, List.of(
                new WeaponLoreRenderEvent.Entry("Reload", "%.1fs".formatted(reload / 20.0))
        ));
    }
}
