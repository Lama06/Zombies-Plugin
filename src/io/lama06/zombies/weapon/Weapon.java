package io.lama06.zombies.weapon;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import io.lama06.zombies.player.ZombiesPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;

public final class Weapon extends Storage {
    public static final AttributeId<Boolean> IS_WEAPON = new AttributeId<>("is_weapon", PersistentDataType.BOOLEAN);

    @Contract("null -> false")
    public static boolean isWeapon(final ItemStack weapon) {
        if (weapon == null) {
            return false;
        }
        final ItemMeta meta = weapon.getItemMeta();
        if (meta == null) {
            return false;
        }
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.getOrDefault(IS_WEAPON.getKey(), PersistentDataType.BOOLEAN, false);
    }

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
