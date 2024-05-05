package io.lama06.zombies;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import io.lama06.zombies.perk.PlayerPerk;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public final class ZombiesPlayer extends Storage implements ForwardingAudience {
    public static final AttributeId<Integer> GAME_ID = new AttributeId<>("game_id", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> GOLD = new AttributeId<>("gold", PersistentDataType.INTEGER);
    public static final AttributeId<Integer> KILLS = new AttributeId<>("kills", PersistentDataType.INTEGER);

    private final Player player;

    public ZombiesPlayer(final Player player) {
        this.player = player;
    }

    public Weapon getWeapon(final int slot) {
        final Weapon weapon = new Weapon(this, slot);
        if (!weapon.isWeapon()) {
            return null;
        }
        return new Weapon(this, slot);
    }

    public Weapon getHeldWeapon() {
        return getWeapon(player.getInventory().getHeldItemSlot());
    }

    public List<Weapon> getWeapons() {
        final List<Weapon> weapons = new ArrayList<>();
        for (int slot = 0; slot < 4; slot++) {
            final Weapon weapon = getWeapon(slot);
            if (weapon == null) {
                continue;
            }
            weapons.add(weapon);
        }
        return weapons;
    }

    public int getLastWeaponSlot() {
        return hasPerk(PlayerPerk.EXTRA_WEAPON) ? 3 : 2;
    }

    public Weapon giveWeapon(final int slot, final WeaponType type) {
        final ItemStack item = new ItemStack(type.data.material);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(type.data.displayName);
        item.setItemMeta(meta);
        final PlayerInventory inventory = player.getInventory();
        inventory.setItem(slot, item);
        final Weapon weapon = new Weapon(this, slot);
        weapon.set(Weapon.IS_WEAPON, true);
        weapon.set(Weapon.TYPE, type);
        Bukkit.getPluginManager().callEvent(new WeaponCreateEvent(weapon, type.data));
        return weapon;
    }

    public boolean isAlive() {
        return player.getGameMode() == GameMode.ADVENTURE;
    }

    public PlayerPerk getPerk(final int slot) {
        final ItemStack item = player.getInventory().getItem(slot);
        if (item == null) {
            return null;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final boolean isPerk = pdc.getOrDefault(PlayerPerk.IS_PLAYER_PERK.getKey(), PlayerPerk.IS_PLAYER_PERK.type(), false);
        if (!isPerk) {
            return null;
        }
        return pdc.get(PlayerPerk.TYPE.getKey(), PlayerPerk.TYPE.type());
    }

    public List<PlayerPerk> getPerks() {
        return IntStream.range(6, 9).mapToObj(this::getPerk).filter(Objects::nonNull).toList();
    }

    public boolean hasPerk(final PlayerPerk type) {
        return getPerks().contains(type);
    }

    public void givePerk(final int slot, final PlayerPerk perk) {
        if (slot < 6 || slot >= 9) {
            throw new IllegalArgumentException();
        }
        final PlayerPerk oldPerk = getPerk(slot);
        if (oldPerk != null) {
            oldPerk.disable(this);
        }
        final ItemStack item = new ItemStack(perk.getDisplayMaterial());
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(perk.getDisplayName());
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(PlayerPerk.TYPE.getKey(), PlayerPerk.TYPE.type(), perk);
        pdc.set(PlayerPerk.IS_PLAYER_PERK.getKey(), PlayerPerk.IS_PLAYER_PERK.type(), true);
        item.setItemMeta(meta);
        player.getInventory().setItem(slot, item);
        perk.enable(this);
    }

    public void clearPerks() {
        final PlayerInventory inventory = player.getInventory();
        for (int slot = 6; slot < 9; slot++) {
            final PlayerPerk perk = getPerk(slot);
            if (perk == null) {
                continue;
            }
            perk.disable(this);
            inventory.setItem(slot, null);
        }
    }

    public boolean requireGold(final int gold) {
        final int currentGold = get(GOLD);
        if (currentGold < gold) {
            sendMessage(Component.text("You cannot afford this").color(NamedTextColor.RED));
            return false;
        }
        return true;
    }

    public void pay(final int gold) {
        final int currentGold = get(GOLD);
        final int newGold = currentGold - gold;
        set(GOLD, newGold);
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(this, currentGold, newGold));
    }

    @Override
    protected StorageSession startSession() {
        return player::getPersistentDataContainer;
    }

    @Override
    public Iterable<? extends Audience> audiences() {
        return Set.of(player);
    }

    public Player getBukkit() {
        return player;
    }

    public ZombiesWorld getWorld() {
        return new ZombiesWorld(player.getWorld());
    }
}
