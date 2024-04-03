package io.lama06.zombies.system.weapon.attack;

import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.AttackData;
import io.lama06.zombies.weapon.Weapon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class GiveGoldAfterAttackSystem implements Listener {
    @EventHandler
    private void onPlayerAttacksZombie(final PlayerAttackZombieEvent event) {
        final Weapon weapon = event.getWeapon();
        final AttackData attackData = weapon.getData().attack;
        if (attackData == null) {
            return;
        }
        final ZombiesPlayer player = event.getPlayer();
        final int goldBefore = player.get(PlayerAttributes.GOLD);
        final int goldAfter = goldBefore + attackData.gold();
        player.set(PlayerAttributes.GOLD, goldAfter);
        player.sendMessage(Component.text("Zombie Attacked: +%s Gold".formatted(attackData.gold())).color(NamedTextColor.GOLD));
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, goldBefore, goldAfter));
    }
}
