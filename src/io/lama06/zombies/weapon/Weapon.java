package io.lama06.zombies.weapon;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.data.ComponentId;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.util.pdc.EnumPersistentDataType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public final class Weapon extends Storage {
    public static final AttributeId<Boolean> IS_WEAPON = new AttributeId<>("is_weapon", PersistentDataType.BOOLEAN);
    public static final AttributeId<WeaponType> TYPE = new AttributeId<>("type", new EnumPersistentDataType<>(WeaponType.class));

    public static final ComponentId AMMO = new ComponentId("ammo");
    public static final ComponentId DELAY = new ComponentId("delay");
    public static final ComponentId RELOAD = new ComponentId("reload");

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
        return Objects.requireNonNullElse(get(IS_WEAPON), false);
    }

    public WeaponType getType() {
        return get(TYPE);
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
