package io.lama06.zombies.weapon.attack;

import io.lama06.zombies.event.PlayerAttacksZombieEvent;
import io.lama06.zombies.weapon.WeaponAttributes;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public final class AttackSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final AttackData data = event.getData().attack();
        if (data == null) {
            return;
        }
        final PersistentDataContainer pdc = event.getPdc();
        final PersistentDataContainer attackContainer = pdc.getAdapterContext().newPersistentDataContainer();
        attackContainer.set(AttackAttributes.DAMAGE.getKey(), PersistentDataType.DOUBLE, data.damage());
        attackContainer.set(AttackAttributes.FIRE.getKey(), PersistentDataType.BOOLEAN, data.fire());
        pdc.set(WeaponAttributes.ATTACK.getKey(), PersistentDataType.TAG_CONTAINER, attackContainer);
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final PersistentDataContainer pdc = event.getWeapon().getItem().getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer container = pdc.get(WeaponAttributes.ATTACK.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final Double damage = container.get(AttackAttributes.DAMAGE.getKey(), PersistentDataType.DOUBLE);
        final Boolean fire = container.get(AttackAttributes.FIRE.getKey(), PersistentDataType.BOOLEAN);
        if (damage == null || fire == null) {
            return;
        }
        final ArrayList<LoreEntry> loreEntries = new ArrayList<>();
        loreEntries.add(new LoreEntry("Damage", damage.toString()));
        if (fire) {
            loreEntries.add(new LoreEntry("Fire", "On"));
        }
        event.addLore(LorePart.ATTACK, loreEntries);
    }

    @EventHandler
    private void applyDamage(final PlayerAttacksZombieEvent event) {
        final ItemStack item = event.getWeapon().getItem();
        if (item == null) {
            return;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer attackContainer = pdc.get(WeaponAttributes.ATTACK.getKey(), PersistentDataType.TAG_CONTAINER);
        if (attackContainer == null) {
            return;
        }
        final Double damage = attackContainer.get(AttackAttributes.DAMAGE.getKey(), PersistentDataType.DOUBLE);
        final Boolean fire = attackContainer.get(AttackAttributes.FIRE.getKey(), PersistentDataType.BOOLEAN);
        if (damage == null || fire == null) {
            return;
        }
        event.setBaseDamage(damage);
        event.setFire(fire);
    }
}
