package io.lama06.zombies.system.perk.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import io.lama06.zombies.perk.PlayerPerk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class RunFlameBulletsPerkSystem implements Listener {
    @EventHandler
    private void onPlayerAttackZombieEvent(final PlayerAttackZombieEvent event) {
        final ZombiesPlayer player = event.getPlayer();
        if (!player.hasPerk(PlayerPerk.FLAME_BULLETS)) {
            return;
        }
        event.setFire(true);
    }
}
