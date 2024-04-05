package io.lama06.zombies.system.perk.global;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.GameEndEvent;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

public final class RemovePerkItemsSystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getGameWorlds()) {
            final Collection<ItemDisplay> itemDisplays = world.getBukkit().getEntitiesByClass(ItemDisplay.class);
            for (final ItemDisplay itemDisplay : itemDisplays) {
                final PersistentDataContainer pdc = itemDisplay.getPersistentDataContainer();
                final Integer remainingTime = pdc.get(PerkItem.getRemainingTimeKey(), PersistentDataType.INTEGER);
                if (remainingTime == null) {
                    continue;
                }
                if (remainingTime == 0) {
                    itemDisplay.remove();
                    continue;
                }
                pdc.set(PerkItem.getRemainingTimeKey(), PersistentDataType.INTEGER, remainingTime - 1);
            }
        }
    }

    @EventHandler
    private void onGameEnd(final GameEndEvent event) {
        final ZombiesWorld world = event.getWorld();
        final Collection<ItemDisplay> displays = world.getBukkit().getEntitiesByClass(ItemDisplay.class);
        for (final ItemDisplay display : displays) {
            final PersistentDataContainer pdc = display.getPersistentDataContainer();
            if (!pdc.has(PerkItem.getPerkNameKey(), PersistentDataType.STRING)) {
                continue;
            }
            display.remove();
        }
    }
}
