package io.lama06.zombies.weapon.delay;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponAttributes;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.event.WeaponUseEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class DelaySystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final Integer delay = event.getData().delay();
        if (delay == null) {
            return;
        }
        final PersistentDataContainer pdc = event.getPdc();
        final PersistentDataContainer delayContainer = pdc.getAdapterContext().newPersistentDataContainer();
        delayContainer.set(DelayAttributes.DELAY.getKey(), PersistentDataType.INTEGER, delay);
        delayContainer.set(DelayAttributes.REMAINING_DELAY.getKey(), PersistentDataType.INTEGER, 0);
        pdc.set(WeaponAttributes.DELAY.getKey(), PersistentDataType.TAG_CONTAINER, delayContainer);
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final PersistentDataContainer pdc = event.getWeapon().getItem().getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer container = pdc.get(WeaponAttributes.DELAY.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final Integer delay = container.get(DelayAttributes.DELAY.getKey(), PersistentDataType.INTEGER);
        if (delay == null) {
            return;
        }
        event.addLore(LorePart.ATTACK, List.of(
                new LoreEntry("Delay", "%.1f".formatted(delay / 20.0))
        ));
    }

    @EventHandler
    private void renderDelay(final PlayerItemHeldEvent event) {
        renderDelay(new Weapon(event.getPlayer(), event.getNewSlot()));
    }

    @EventHandler
    private void renderDelay(final WeaponDelayChangeEvent event) {
        renderDelay(new Weapon(event.getPlayer(), event.getPlayer().getInventory().getHeldItemSlot()));
    }

    private void renderDelay(final Weapon weapon) {
        final Player owner = weapon.owner();
        final ItemStack item = weapon.getItem();
        if (item == null) {
            owner.setExp(0);
            return;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer delayContainer = pdc.get(WeaponAttributes.DELAY.getKey(), PersistentDataType.TAG_CONTAINER);
        if (delayContainer == null) {
            owner.setExp(0);
            return;
        }
        final Integer delay = delayContainer.get(DelayAttributes.DELAY.getKey(), PersistentDataType.INTEGER);
        final Integer remainingDelay = delayContainer.get(DelayAttributes.REMAINING_DELAY.getKey(), PersistentDataType.INTEGER);
        if (delay == null || remainingDelay == null) {
            owner.setExp(0);
            return;
        }
        final float progress = (float) (delay - remainingDelay) / delay;
        owner.setExp(progress);
    }

    @EventHandler
    private void tickDelay(final ServerTickEndEvent event) {
        for (final Weapon weapon : Weapon.getAllWeapons()) {
            final ItemStack item = weapon.getItem();
            if (item == null) {
                continue;
            }
            final ItemMeta meta = item.getItemMeta();
            final PersistentDataContainer pdc = meta.getPersistentDataContainer();
            final PersistentDataContainer delayContainer = pdc.get(WeaponAttributes.DELAY.getKey(), PersistentDataType.TAG_CONTAINER);
            if (delayContainer == null) {
                continue;
            }
            final Integer delay = delayContainer.get(DelayAttributes.DELAY.getKey(), PersistentDataType.INTEGER);
            final Integer remainingDelay = delayContainer.get(DelayAttributes.REMAINING_DELAY.getKey(), PersistentDataType.INTEGER);
            if (delay == null || remainingDelay == null) {
                continue;
            }
            if (remainingDelay == 0) {
                continue;
            }
            delayContainer.set(DelayAttributes.REMAINING_DELAY.getKey(), PersistentDataType.INTEGER, remainingDelay - 1);
            pdc.set(WeaponAttributes.DELAY.getKey(), PersistentDataType.TAG_CONTAINER, delayContainer);
            item.setItemMeta(meta);
            Bukkit.getPluginManager().callEvent(new WeaponDelayChangeEvent(weapon, remainingDelay, remainingDelay - 1));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void preventWeaponUseDuringDelay(final WeaponUseEvent event) {
        final ItemStack item = event.getWeapon().getItem();
        if (item == null) {
            return;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer delayContainer = pdc.get(WeaponAttributes.DELAY.getKey(), PersistentDataType.TAG_CONTAINER);
        if (delayContainer == null) {
            return;
        }
        final Integer remainingDelay = delayContainer.get(DelayAttributes.REMAINING_DELAY.getKey(), PersistentDataType.INTEGER);
        if (remainingDelay == null || remainingDelay == 0) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void startDelayAfterWeaponUse(final WeaponUseEvent event) {
        final ItemStack item = event.getWeapon().getItem();
        if (item == null) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        final PersistentDataContainer delayContainer = pdc.get(WeaponAttributes.DELAY.getKey(), PersistentDataType.TAG_CONTAINER);
        if (delayContainer == null) {
            return;
        }
        final Integer delay = delayContainer.get(DelayAttributes.DELAY.getKey(), PersistentDataType.INTEGER);
        final Integer remainingDelay = delayContainer.get(DelayAttributes.REMAINING_DELAY.getKey(), PersistentDataType.INTEGER);
        if (delay == null || remainingDelay == null) {
            return;
        }
        delayContainer.set(DelayAttributes.REMAINING_DELAY.getKey(), PersistentDataType.INTEGER, delay);
        pdc.set(WeaponAttributes.DELAY.getKey(), PersistentDataType.TAG_CONTAINER, delayContainer);
        item.setItemMeta(meta);
        Bukkit.getPluginManager().callEvent(new WeaponDelayChangeEvent(event.getWeapon(), remainingDelay, delay));
    }
}
