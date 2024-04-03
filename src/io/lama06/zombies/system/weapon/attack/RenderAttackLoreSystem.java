package io.lama06.zombies.system.weapon.attack;

import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.AttackData;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public final class RenderAttackLoreSystem implements Listener {
    @EventHandler
    private void onWeaponLoreRender(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final AttackData attackData = weapon.getData().attack;
        if (attackData == null) {
            return;
        }
        final List<WeaponLoreRenderEvent.Entry> loreEntries = new ArrayList<>();
        loreEntries.add(new WeaponLoreRenderEvent.Entry("Damage", Double.toString(attackData.damage())));
        if (attackData.fire()) {
            loreEntries.add(new WeaponLoreRenderEvent.Entry("Fire", "On"));
        }
        event.addLore(WeaponLoreRenderEvent.Part.ATTACK, loreEntries);
    }

}
