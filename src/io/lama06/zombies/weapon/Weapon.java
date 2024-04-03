package io.lama06.zombies.weapon;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import io.lama06.zombies.player.ZombiesPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public final class Weapon extends Storage {
    private final ZombiesPlayer player;
    private final int slot;

    public Weapon(final ZombiesPlayer player, final int slot) {
        this.player = player;
        this.slot = slot;
    }

    public ZombiesPlayer getPlayer() {
        return player;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return player.getBukkit().getInventory().getItem(slot);
    }

    public ZombiesWorld getWorld() {
        return new ZombiesWorld(player.getBukkit().getWorld());
    }

    public boolean isWeapon() {
        final ItemStack item = getItem();
        if (item == null) {
            return false;
        }
        if (item.getItemMeta() == null) {
            return false;
        }
        return getOrDefault(WeaponAttributes.IS_WEAPON, false);
    }

    public WeaponType getType() {
        return get(WeaponAttributes.TYPE);
    }

    public WeaponData getData() {
        return getType().data;
    }

    @Override
    protected StorageSession startSession() {
        final ItemStack item = getItem();
        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return new StorageSession() {
            @Override
            public PersistentDataContainer getData() {
                return pdc;
            }

            @Override
            public void applyChanges() {
                item.setItemMeta(meta);
            }
        };
    }
}
