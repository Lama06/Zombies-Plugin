package io.lama06.zombies.system.perk.global;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.perk.GlobalPerk;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.event.perk.PlayerPickupPerkItemEvent;
import io.lama06.zombies.ZombiesPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

public final class PickupPerkItemsSystem implements Listener {
    private static final double PICKUP_RADIUS = 3;

    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final ZombiesPlayer player : ZombiesPlugin.INSTANCE.getAlivePlayers()) {
            final Collection<ItemDisplay> nearbyDisplayItems = player.getBukkit().getWorld().getNearbyEntitiesByType(
                    ItemDisplay.class,
                    player.getBukkit().getLocation(),
                    PICKUP_RADIUS
            );
            for (final ItemDisplay nearbyDisplayItem : nearbyDisplayItems) {
                final PersistentDataContainer pdc = nearbyDisplayItem.getPersistentDataContainer();
                final String perkName = pdc.get(PerkItem.getPerkNameKey(), PersistentDataType.STRING);
                if (perkName == null) {
                    continue;
                }
                final GlobalPerk perk;
                try {
                    perk = GlobalPerk.valueOf(perkName);
                } catch (final IllegalArgumentException e) {
                    continue;
                }
                Bukkit.getPluginManager().callEvent(new PlayerPickupPerkItemEvent(player, perk));
                final Component announcement = player.getBukkit().displayName()
                        .append(Component.text(" picked up "))
                        .append(perk.getDisplayName());
                player.getWorld().sendMessage(announcement);
                nearbyDisplayItem.remove();
            }
        }
    }
}
