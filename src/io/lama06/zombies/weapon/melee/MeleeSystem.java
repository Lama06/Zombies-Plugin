package io.lama06.zombies.weapon.melee;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.PlayerAttacksZombieEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import io.lama06.zombies.zombie.Zombie;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public final class MeleeSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final MeleeData data = event.getData().melee();
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component meleeComponent = weapon.addComponent(WeaponComponents.MELEE);
        meleeComponent.set(MeleeAttributes.RANGE, data.range());
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component meleeComponent = weapon.getComponent(WeaponComponents.MELEE);
        if (meleeComponent == null) {
            return;
        }
        final double range = meleeComponent.get(MeleeAttributes.RANGE);
        event.addLore(LorePart.MELEE, List.of(
                new LoreEntry("Range", "%.1f".formatted(range))
        ));
    }

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
        Bukkit.getPluginManager().callEvent(new PlayerAttacksZombieEvent(weapon, new Zombie(entity)));
    }
}
