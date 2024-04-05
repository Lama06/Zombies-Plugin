package io.lama06.zombies.system.perk.global;

import io.lama06.zombies.perk.GlobalPerk;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.perk.PlayerPickupPerkItemEvent;
import io.lama06.zombies.ZombiesPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class EnableTemporaryPerksSystem implements Listener {
    @EventHandler
    private void onPlayerPickupPerk(final PlayerPickupPerkItemEvent event) {
        final GlobalPerk perk = event.getPerk();
        if (perk.getTime() == 0) {
            return;
        }
        final ZombiesPlayer player = event.getPlayer();
        final ZombiesWorld world = player.getWorld();
        final Component perksComponent = world.getComponent(ZombiesWorld.PERKS_COMPONENT);
        if (perksComponent == null) {
            return;
        }
        perksComponent.set(perk.getRemainingTimeAttribute(), perk.getTime());
    }
}
