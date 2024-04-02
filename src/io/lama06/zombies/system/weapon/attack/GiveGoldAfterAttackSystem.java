package io.lama06.zombies.system.weapon.attack;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.AttackData;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class GiveGoldAfterAttackSystem implements Listener {
    @EventHandler
    private void onPlayerAttacksZombie(final PlayerAttackZombieEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component attackComponent = weapon.getComponent(WeaponComponents.ATTACK);
        if (attackComponent == null) {
            return;
        }
        final int gold = attackComponent.get(AttackData.GOLD);
        final ZombiesPlayer player = event.getPlayer();
        final int goldBefore = player.get(PlayerAttributes.GOLD);
        final int goldAfter = goldBefore + gold;
        player.set(PlayerAttributes.GOLD, goldAfter);
        player.sendMessage(net.kyori.adventure.text.Component.text("Zombie Attacked: +%s Gold".formatted(gold))
                                   .color(NamedTextColor.GOLD));
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, goldBefore, goldAfter));
    }
}
