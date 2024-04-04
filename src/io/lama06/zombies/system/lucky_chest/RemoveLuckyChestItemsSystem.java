package io.lama06.zombies.system.lucky_chest;

import io.lama06.zombies.event.GameEndEvent;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class RemoveLuckyChestItemsSystem implements Listener {
    @EventHandler
    private void onGameEnded(final GameEndEvent event) {
        for (final ItemDisplay itemDisplay : event.getWorld().getBukkit().getEntitiesByClass(ItemDisplay.class)) {
            final PersistentDataContainer pdc = itemDisplay.getPersistentDataContainer();
            final Integer remainingTime = pdc.get(LuckyChestItem.getRemainingTimeKey(), PersistentDataType.INTEGER);
            if (remainingTime == null) {
                continue;
            }
            itemDisplay.remove();
        }
    }
}
