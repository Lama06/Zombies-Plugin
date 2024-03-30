package io.lama06.zombies.weapon.render;

import io.lama06.zombies.weapon.Weapon;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public final class LoreRenderSystem implements Listener {
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
                .map(LoreEntry::toComponent)
                .toList();
        final ItemMeta meta = item.getItemMeta();
        meta.lore(lore);
        item.setItemMeta(meta);
    }
}
