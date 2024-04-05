package io.lama06.zombies.system.perk.global;

import io.lama06.zombies.perk.GlobalPerk;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.player.PlayerAttackZombieEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PerformInstantKillPerkSystem implements Listener {
    @EventHandler
    private void onPlayerAttacksZombie(final PlayerAttackZombieEvent event) {
        final ZombiesWorld world = event.getWorld();
        final Component perksComponent = world.getComponent(ZombiesWorld.PERKS_COMPONENT);
        if (perksComponent == null) {
            return;
        }
        final int instantKillTime = perksComponent.get(GlobalPerk.INSTANT_KILL.getRemainingTimeAttribute());
        if (instantKillTime == 0) {
            return;
        }
        event.setKill(true);
    }
}
