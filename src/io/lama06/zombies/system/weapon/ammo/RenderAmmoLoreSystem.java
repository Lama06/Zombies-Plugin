package io.lama06.zombies.system.weapon.ammo;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.AmmoData;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.event.weapon.WeaponAmmoChangeEvent;
import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import io.lama06.zombies.system.weapon.RenderWeaponLoreSystem;
import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class RenderAmmoLoreSystem implements Listener {
    @EventHandler
    private void onWeaponAmmoChange(final WeaponAmmoChangeEvent event) {
        RenderWeaponLoreSystem.renderLore(event.getWeapon());
    }

    @EventHandler
    private void onWeaponClipChange(final WeaponClipChangeEvent event) {
        RenderWeaponLoreSystem.renderLore(event.getWeapon());
    }

    @EventHandler
    private void onWeaponLoreRender(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component component = weapon.getComponent(WeaponComponents.AMMO);
        if (component == null) {
            return;
        }
        final int maxAmmo = weapon.getData().ammo.ammo();
        final int ammo = component.get(AmmoData.AMMO);
        final int maxClip = weapon.getData().ammo.clip();
        final int clip = component.get(AmmoData.CLIP);
        event.addLore(WeaponLoreRenderEvent.Part.AMMO, List.of(
                new WeaponLoreRenderEvent.Entry("Ammo", ammo + " / " + maxAmmo),
                new WeaponLoreRenderEvent.Entry("Clip", clip + " / " + maxClip)
        ));
    }
}
