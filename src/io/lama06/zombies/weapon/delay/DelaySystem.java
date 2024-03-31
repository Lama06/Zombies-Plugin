package io.lama06.zombies.weapon.delay;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.event.WeaponUseEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.List;

public final class DelaySystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final Integer delay = event.getData().delay();
        if (delay == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.addComponent(WeaponComponents.DELAY);
        delayComponent.set(DelayAttributes.DELAY, delay);
        delayComponent.set(DelayAttributes.REMAINING_DELAY, 0);
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.getComponent(WeaponComponents.DELAY);
        if (delayComponent == null) {
            return;
        }
        final int delay = delayComponent.get(DelayAttributes.DELAY);
        event.addLore(LorePart.DELAY, List.of(
                new LoreEntry("Delay", "%.1f".formatted(delay / 20.0))
        ));
    }

    @EventHandler
    private void renderDelay(final PlayerItemHeldEvent event) {
        if (!ZombiesPlayer.isZombiesPlayer(event.getPlayer())) {
            return;
        }
        renderDelay(new ZombiesPlayer(event.getPlayer()), event.getNewSlot());
    }

    @EventHandler
    private void renderDelay(final WeaponDelayChangeEvent event) {
        renderDelay(event.getPlayer(), event.getPlayer().getBukkit().getInventory().getHeldItemSlot());
    }

    private void renderDelay(final ZombiesPlayer player, final int slot) {
        final Weapon weapon = player.getWeapon(slot);
        if (weapon == null) {
            player.getBukkit().setExp(0);
            return;
        }
        final Component delayComponent = weapon.getComponent(WeaponComponents.DELAY);
        if (delayComponent == null) {
            player.getBukkit().setExp(0);
            return;
        }
        final int delay = delayComponent.get(DelayAttributes.DELAY);
        final int remainingDelay = delayComponent.get(DelayAttributes.REMAINING_DELAY);
        final float progress = (float) (delay - remainingDelay) / delay;
        player.getBukkit().setExp(progress);
    }

    @EventHandler
    private void tickDelay(final ServerTickEndEvent event) {
        for (final Weapon weapon : ZombiesPlugin.INSTANCE.getWeapons()) {
            final Component delayComponent = weapon.getComponent(WeaponComponents.DELAY);
            if (delayComponent == null) {
                continue;
            }
            final int remainingDelay = delayComponent.get(DelayAttributes.REMAINING_DELAY);
            if (remainingDelay == 0) {
                continue;
            }
            delayComponent.set(DelayAttributes.REMAINING_DELAY, remainingDelay - 1);
            Bukkit.getPluginManager().callEvent(new WeaponDelayChangeEvent(weapon, remainingDelay, remainingDelay - 1));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void preventWeaponUseDuringDelay(final WeaponUseEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.getComponent(WeaponComponents.DELAY);
        if (delayComponent == null) {
            return;
        }
        final int remainingDelay = delayComponent.get(DelayAttributes.REMAINING_DELAY);
        if (remainingDelay > 0) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void startDelayAfterWeaponUse(final WeaponUseEvent event) {
        final Weapon weapon = event.getWeapon();
        final Component delayComponent = weapon.getComponent(WeaponComponents.DELAY);
        if (delayComponent == null) {
            return;
        }
        final int delay = delayComponent.get(DelayAttributes.DELAY);
        final int remainingDelay = delayComponent.get(DelayAttributes.REMAINING_DELAY);
        delayComponent.set(DelayAttributes.REMAINING_DELAY, delay);
        Bukkit.getPluginManager().callEvent(new WeaponDelayChangeEvent(event.getWeapon(), remainingDelay, delay));
    }
}
