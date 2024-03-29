package io.lama06.zombies.weapon.delay;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.Weapon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;

public final class DelayRenderSystem extends System {
    public DelayRenderSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void onPlayerHoldsItem(final PlayerItemHeldEvent event) {
        if (!game.getPlayers().containsKey(event.getPlayer())) {
            return;
        }
        renderDelay(game.getPlayers().get(event.getPlayer()), event.getNewSlot());
    }

    @EventHandler
    private void handleDelayChanged(final WeaponDelayChangeEvent event) {
        final ZombiesPlayer player = event.getPlayer();
        if (!game.getPlayers().containsValue(player)) {
            return;
        }
        renderDelay(player, player.getBukkit().getInventory().getHeldItemSlot());
    }

    private void renderDelay(final ZombiesPlayer player, final int currentSlot) {
        if (currentSlot >= player.getWeapons().size()) {
            player.getBukkit().setExp(0);
            return;
        }
        final Weapon weapon = player.getWeapons().get(currentSlot);
        if (weapon.getDelay() == null) {
            player.getBukkit().setExp(0);
            return;
        }
        final float progress = (float) (weapon.getDelay().getDelay() - weapon.getDelay().getRemainingDelay()) / weapon.getDelay().getDelay();
        player.getBukkit().setExp(progress);
    }
}
