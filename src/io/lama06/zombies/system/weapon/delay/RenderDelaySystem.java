package io.lama06.zombies.system.weapon.delay;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.weapon.DelayAttributes;
import io.lama06.zombies.event.weapon.WeaponDelayChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public final class RenderDelaySystem implements Listener {
    @EventHandler
    private void onPlayerItemHeld(final PlayerItemHeldEvent event) {
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        if (!player.getWorld().isGameRunning() || !player.isAlive()) {
            return;
        }
        renderDelay(player, event.getNewSlot());
    }

    @EventHandler
    private void onWeaponDelayChange(final WeaponDelayChangeEvent event) {
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
}
