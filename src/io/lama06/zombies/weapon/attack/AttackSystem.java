package io.lama06.zombies.weapon.attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.PlayerAttacksZombieEvent;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public final class AttackSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final AttackData data = event.getData().attack();
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component attackComponent = weapon.addComponent(WeaponComponents.ATTACK);
        attackComponent.set(AttackAttributes.DAMAGE, data.damage());
        attackComponent.set(AttackAttributes.FIRE, data.fire());
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component attackComponent = weapon.getComponent(WeaponComponents.ATTACK);
        if (attackComponent == null) {
            return;
        }
        final boolean fire = attackComponent.get(AttackAttributes.FIRE);
        final double damage = attackComponent.get(AttackAttributes.DAMAGE);
        final ArrayList<LoreEntry> loreEntries = new ArrayList<>();
        loreEntries.add(new LoreEntry("Damage", Double.toString(damage)));
        if (fire) {
            loreEntries.add(new LoreEntry("Fire", "On"));
        }
        event.addLore(LorePart.ATTACK, loreEntries);
    }

    @EventHandler
    private void applyDamage(final PlayerAttacksZombieEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component attackComponent = weapon.getComponent(WeaponComponents.ATTACK);
        if (attackComponent == null) {
            return;
        }
        final boolean fire = attackComponent.get(AttackAttributes.FIRE);
        final double damage = attackComponent.get(AttackAttributes.DAMAGE);
        event.setBaseDamage(damage);
        event.setFire(fire);
    }
}
