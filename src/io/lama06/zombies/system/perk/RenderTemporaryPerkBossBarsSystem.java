package io.lama06.zombies.system.perk;

import io.lama06.zombies.GlobalPerk;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.GameEndEvent;
import io.lama06.zombies.event.perk.PerkTickEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public final class RenderTemporaryPerkBossBarsSystem implements Listener {
    private final Map<World, Map<GlobalPerk, BossBar>> bossBars = new HashMap<>();

    private void setupBossbars(final ZombiesWorld world) {
        if (bossBars.containsKey(world.getBukkit())) {
            return;
        }
        final Map<GlobalPerk, BossBar> worldBossBars = new HashMap<>();
        bossBars.put(world.getBukkit(), worldBossBars);
        for (final GlobalPerk perk : GlobalPerk.values()) {
            final BossBar bossBar = Bukkit.createBossBar(
                    LegacyComponentSerializer.legacySection().serialize(perk.getDisplayName()),
                    perk.getBarColor(),
                    BarStyle.SOLID
            );
            worldBossBars.put(perk, bossBar);
        }
    }

    @EventHandler
    private void onGameEnd(final GameEndEvent event) {
        final ZombiesWorld world = event.getWorld();
        final Map<GlobalPerk, BossBar> worldBossBars = bossBars.get(world.getBukkit());
        if (worldBossBars == null) {
            return;
        }
        for (final BossBar bossBar : worldBossBars.values()) {
            bossBar.removeAll();
        }
    }

    @EventHandler
    private void onPerkTick(final PerkTickEvent event) {
        final ZombiesWorld world = event.getWorld();
        setupBossbars(world);
        final GlobalPerk perk = event.getPerk();
        final BossBar bossBar = bossBars.get(world.getBukkit()).get(perk);
        if (event.getRemainingTime() == 0) {
            bossBar.removeAll();
            return;
        }
        for (final ZombiesPlayer player : world.getPlayers()) {
            bossBar.addPlayer(player.getBukkit());
        }
        bossBar.setProgress((double) event.getRemainingTime() / perk.getTime());
    }
}
