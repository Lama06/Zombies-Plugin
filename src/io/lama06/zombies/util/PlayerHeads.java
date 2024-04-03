package io.lama06.zombies.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public enum PlayerHeads {
    MAGMA_CUBE("EvModder");

    private final String playerName;

    PlayerHeads(final String playerName) {
        this.playerName = playerName;
    }

    public ItemStack createItem() {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        final ItemMeta meta = item.getItemMeta();
        final SkullMeta skull = (SkullMeta) meta;
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        item.setItemMeta(meta);
        return item;
    }
}
