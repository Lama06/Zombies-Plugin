package io.lama06.zombies.system.weapon.reload;

import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.ReloadData;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RenderReloadLoreSystem implements Listener {
    @EventHandler
    private void onWeaponLoreRender(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final ReloadData reloadData = weapon.getData().reload;
        if (reloadData == null) {
            return;
        }
        event.addLore(WeaponLoreRenderEvent.Part.RELOAD, List.of(
                new WeaponLoreRenderEvent.Entry("Reload", "%.1fs".formatted(reloadData.reload() / 20.0))
        ));
    }
}
