package io.lama06.zombies.player;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponAttributes;
import io.lama06.zombies.weapon.WeaponType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ZombiesPlayer extends Storage implements ForwardingAudience {
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

    public Weapon giveWeapon(final int slot, final WeaponType type) {
        final ItemStack item = new ItemStack(type.data.material);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(type.data.displayName);
        item.setItemMeta(meta);
        final PlayerInventory inventory = player.getInventory();
        inventory.setItem(slot, item);
        final Weapon weapon = new Weapon(this, slot);
        weapon.set(WeaponAttributes.IS_WEAPON, true);
        weapon.set(WeaponAttributes.TYPE, type);
        Bukkit.getPluginManager().callEvent(new WeaponCreateEvent(weapon, type.data));
        return weapon;
    }

    public boolean isAlive() {
        return player.getGameMode() == GameMode.ADVENTURE;
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
