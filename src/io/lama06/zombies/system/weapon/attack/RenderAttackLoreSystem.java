package io.lama06.zombies.system.weapon.attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import io.lama06.zombies.weapon.AttackAttributes;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public final class RenderAttackLoreSystem implements Listener {
    @EventHandler
    private void onWeaponLoreRender(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component attackComponent = weapon.getComponent(WeaponComponents.ATTACK);
        if (attackComponent == null) {
            return;
        }
        final boolean fire = attackComponent.get(AttackAttributes.FIRE);
        final double damage = attackComponent.get(AttackAttributes.DAMAGE);
        final List<WeaponLoreRenderEvent.Entry> loreEntries = new ArrayList<>();
        loreEntries.add(new WeaponLoreRenderEvent.Entry("Damage", Double.toString(damage)));
        if (fire) {
            loreEntries.add(new WeaponLoreRenderEvent.Entry("Fire", "On"));
        }
        event.addLore(WeaponLoreRenderEvent.Part.ATTACK, loreEntries);
    }

}
