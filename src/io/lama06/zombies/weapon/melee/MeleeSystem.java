package io.lama06.zombies.weapon.melee;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public final class MeleeSystem extends System {
    public MeleeSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onPlayerAttackEntity(final PrePlayerAttackEntityEvent event) {
        if (!game.getPlayers().containsKey(event.getPlayer())) {
            return;
        }
        final ZombiesPlayer player = game.getPlayers().get(event.getPlayer());
        final Weapon heldWeapon = player.getHeldWeapon();
        if (heldWeapon == null) {
            return;
        }
        final MeleeComponent melee = heldWeapon.getMelee();
        if (melee == null) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new WeaponMeleeEvent(heldWeapon));
    }
}
