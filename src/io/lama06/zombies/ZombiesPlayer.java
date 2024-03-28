package io.lama06.zombies;

import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ZombiesPlayer implements Listener {
    private final ZombiesGame game;
    private final Player player;
    private final List<Weapon> weapons = new ArrayList<>();
    private int gold;

    public ZombiesPlayer(final ZombiesGame game, final Player player) {
        this.game = game;
        this.player = player;
        weapons.add(new Weapon(this, WeaponType.PISTOL.data));
    }

    public ZombiesGame getGame() {
        return game;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public @Nullable Weapon getHeldWeapon() {
        final int slot = player.getInventory().getHeldItemSlot();
        if (slot >= weapons.size()) {
            return null;
        }
        return weapons.get(slot);
    }

    public Player getBukkit() {
        return player;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(final int gold) {
        this.gold = gold;
    }
}
