package io.lama06.zombies.system.weapon.melee;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.event.weapon.WeaponMeleeEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.MeleeAttributes;
import io.lama06.zombies.zombie.Zombie;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class AttackMeleeSystem implements Listener {
    @EventHandler
    private void onPlayerAttackEntity(final PrePlayerAttackEntityEvent event) {
        final Player bukkit = event.getPlayer();
        if (!ZombiesPlayer.isZombiesPlayer(bukkit)) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(bukkit);
        final Weapon weapon = player.getHeldWeapon();
        if (weapon == null) {
            return;
        }
        final Component meleeComponent = weapon.getComponent(WeaponComponents.MELEE);
        if (meleeComponent == null) {
            return;
        }
        final double range = meleeComponent.get(MeleeAttributes.RANGE);
        if (!new WeaponMeleeEvent(weapon).callEvent()) {
            return;
        }
        final Entity entity = player.getBukkit().getTargetEntity((int) range);
        if (!Zombie.isZombie(entity)) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerAttackZombieEvent(weapon, new Zombie(entity)));
    }
}
