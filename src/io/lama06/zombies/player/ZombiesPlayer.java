package io.lama06.zombies.player;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponData;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ZombiesPlayer extends Storage implements ForwardingAudience {
    public static boolean isZombiesPlayer(final Player player) {
        return player != null && ZombiesWorld.isGameWorld(player.getWorld());
    }

    private final Player player;

    public ZombiesPlayer(final Player player) {
        this.player = player;
    }

    public @Nullable Weapon getWeapon(final int slot) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack item = inventory.getItem(slot);
        if (!Weapon.isWeapon(item)) {
            return null;
        }
        return new Weapon(this, slot);
    }

    public @Nullable Weapon getHeldWeapon() {
        return getWeapon(player.getInventory().getHeldItemSlot());
    }

    public @NotNull List<Weapon> getWeapons() {
        final List<Weapon> weapons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final Weapon weapon = getWeapon(i);
            if (weapon == null) {
                continue;
            }
            weapons.add(weapon);
        }
        return weapons;
    }

    public Weapon giveWeapon(final int slot, final WeaponData data) {
        final ItemStack item = new ItemStack(data.material());
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(data.displayName());
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(Weapon.IS_WEAPON.getKey(), PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        final PlayerInventory inventory = player.getInventory();
        inventory.setItem(slot, item);
        final Weapon weapon = new Weapon(this, slot);
        Bukkit.getPluginManager().callEvent(new WeaponCreateEvent(weapon, data));
        return weapon;
    }

    @Override
    protected StorageSession startSession() {
        return player::getPersistentDataContainer;
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return Set.of(player);
    }

    public Player getBukkit() {
        return player;
    }

    public ZombiesWorld getWorld() {
        return new ZombiesWorld(player.getWorld());
    }
}
