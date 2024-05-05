package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class EndGameWhenPlayersDeadSystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getWorlds()) {
            if (!world.isZombiesWorld()) {
                return;
            }
            if (!world.getConfig().autoStartStop) {
                return;
            }
            if (!world.isGameRunning()) {
                continue;
            }
            if (!world.getAlivePlayers().isEmpty()) {
                continue;
            }
            world.sendMessage(Component.text("Failed").color(NamedTextColor.RED));
            world.endGame();
        }
    }
}
