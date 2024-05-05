package io.lama06.zombies.system;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.InvalidConfigException;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Duration;

public final class StartGameTimerSystem implements Listener {
    private static final int START_TIME = 10 * 20;

    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getWorlds()) {
            if (!world.isZombiesWorld()) {
                continue;
            }
            if (!world.getConfig().autoStartStop) {
                world.remove(ZombiesWorld.START_TIMER);
                continue;
            }
            try {
                world.getConfig().check();
            } catch (final InvalidConfigException e) {
                world.remove(ZombiesWorld.START_TIMER);
                continue;
            }
            if (world.isGameRunning()) {
                world.remove(ZombiesWorld.START_TIMER);
                continue;
            }
            if (world.getPlayers().isEmpty()) {
                world.remove(ZombiesWorld.START_TIMER);
                continue;
            }
            final Integer startTimer = world.get(ZombiesWorld.START_TIMER);
            if (startTimer == null) {
                world.set(ZombiesWorld.START_TIMER, START_TIME);
                continue;
            }
            if (startTimer == 0) {
                world.startGame();
                world.remove(ZombiesWorld.START_TIMER);
                continue;
            }
            world.set(ZombiesWorld.START_TIMER, startTimer - 1);
            if (startTimer % 20 == 0) {
                world.showTitle(Title.title(
                        Component.text(startTimer / 20),
                        Component.empty(),
                        Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)
                ));
            }
        }
    }
}
