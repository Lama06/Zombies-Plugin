package io.lama06.zombies.system.weapon;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import io.lama06.zombies.event.weapon.WeaponLoreRenderEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public final class RenderWeaponLoreSystem implements Listener {
    public static void renderLore(final Weapon weapon) {
        final ItemStack item = weapon.getItem();
        if (item == null) {
            return;
        }
        final WeaponLoreRenderEvent event = new WeaponLoreRenderEvent(weapon);
        Bukkit.getPluginManager().callEvent(event);
        final List<Component> lore = event.getLore().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .map(WeaponLoreRenderEvent.Entry::toComponent)
                .toList();
        final ItemMeta meta = item.getItemMeta();
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    @EventHandler(
            priority = EventPriority.HIGH // call event after the weapon's components are initialised
    )
    private void renderLoreOnWeaponCreation(final WeaponCreateEvent event) {
        final Weapon weapon = event.getWeapon();
        renderLore(weapon);
    }
}
