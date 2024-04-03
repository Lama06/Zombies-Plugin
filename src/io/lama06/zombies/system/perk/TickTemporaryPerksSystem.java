package io.lama06.zombies.system.perk;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.GlobalPerk;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.perk.PerkTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class TickTemporaryPerksSystem implements Listener {
    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getGameWorlds()) {
            final Component perksComponent = world.getComponent(ZombiesWorld.PERKS_COMPONENT);
            if (perksComponent == null) {
                continue;
            }
            for (final GlobalPerk perk : GlobalPerk.values()) {
                final int time = perksComponent.get(perk.getRemainingTimeAttribute());
                if (time == 0) {
                    continue;
                }
                perksComponent.set(perk.getRemainingTimeAttribute(), time - 1);
                Bukkit.getPluginManager().callEvent(new PerkTickEvent(world, perk, time - 1));
            }
        }
    }
}
