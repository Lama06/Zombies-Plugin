package io.lama06.zombies.weapon.melee;

import io.lama06.zombies.event.PlayerAttacksZombieEvent;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponAttributes;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class MeleeSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final MeleeData data = event.getData().melee();
        if (data == null) {
            return;
        }
        final PersistentDataContainer pdc = event.getPdc();
        final PersistentDataContainer meleeContainer = pdc.getAdapterContext().newPersistentDataContainer();
        meleeContainer.set(MeleeAttributes.RANGE.getKey(), PersistentDataType.DOUBLE, data.range());
        pdc.set(WeaponAttributes.MELEE.getKey(), PersistentDataType.TAG_CONTAINER, meleeContainer);
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final PersistentDataContainer pdc = event.getWeapon().getItem().getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer container = pdc.get(WeaponAttributes.MELEE.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final Double range = container.get(MeleeAttributes.RANGE.getKey(), PersistentDataType.DOUBLE);
        if (range == null) {
            return;
        }
        event.addLore(LorePart.MEELE, List.of(
                new LoreEntry("Range", "%.1f".formatted(range))
        ));
    }

    @EventHandler
    private void onPlayerAttackEntity(final PrePlayerAttackEntityEvent event) {
        final Player player = event.getPlayer();
        final Weapon weapon = Weapon.getHeldWeapon(player);
        if (weapon == null) {
            return;
        }
        final ItemStack item = weapon.getItem();
        if (item == null) {
            return;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer meleeContainer = pdc.get(WeaponAttributes.MELEE.getKey(), PersistentDataType.TAG_CONTAINER);
        if (meleeContainer == null) {
            return;
        }
        final Double range = meleeContainer.get(MeleeAttributes.RANGE.getKey(), PersistentDataType.DOUBLE);
        if (range == null) {
            return;
        }
        if (!new WeaponMeleeEvent(weapon).callEvent()) {
            return;
        }
        final Entity zombie = player.getTargetEntity((int) range.doubleValue());
        if (zombie == null) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerAttacksZombieEvent(weapon, zombie));
    }
}
