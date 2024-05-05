package io.lama06.zombies.system.perk.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.perk.PlayerPerk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class RunFrozenBulletsPerkSystem implements Listener {
    @EventHandler
    private void onPlayerAttacksZombie(final PlayerAttackZombieEvent event) {
        final ZombiesPlayer player = event.getPlayer();
        if (!player.hasPerk(PlayerPerk.FROZEN_BULLETS)) {
            return;
        }
        event.setFreeze(true);
    }
}
