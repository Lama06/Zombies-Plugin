package io.lama06.zombies.system.player;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.GameEndEvent;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.event.StartRoundEvent;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.event.player.PlayerKillZombieEvent;
import io.lama06.zombies.event.player.PlayerKillsIncrementEvent;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public final class RenderScoreboardSystem implements Listener {
    private static List<Component> getSidebarContent(final ZombiesPlayer player) {
        final List<Component> content = new ArrayList<>();
        final ZombiesWorld world = player.getWorld();

        final int round = world.get(ZombiesWorld.ROUND);
        content.add(Component.text("Round " + round).color(NamedTextColor.RED));

        final int aliveZombies = world.getZombies().size();
        final int remainingZombies = world.get(ZombiesWorld.REMAINING_ZOMBIES);
        final int zombiesLeft = aliveZombies + remainingZombies;
        content.add(Component.text("Zombies Left: ").append(Component.text(zombiesLeft).color(NamedTextColor.GREEN)));

        content.add(Component.empty());

        content.add(Component.text("Gold:").color(NamedTextColor.GOLD));
        for (final ZombiesPlayer otherPlayer : world.getPlayers()) {
            final int coins = otherPlayer.get(ZombiesPlayer.GOLD);
            final TextComponent.Builder coinsComponent = Component.text();
            coinsComponent.append(Component.text(otherPlayer.getBukkit().getName()));
            coinsComponent.append(Component.text(": "));
            coinsComponent.append(Component.text(coins).color(NamedTextColor.GOLD));
            content.add(coinsComponent.asComponent());
        }

        content.add(Component.empty());

        final int kills = player.get(ZombiesPlayer.KILLS);
        content.add(Component.text("Zombie Kills: ").append(Component.text(kills).color(NamedTextColor.GREEN)));

        return content;
    }

    private static void updateSidebar(final ZombiesPlayer player, final Scoreboard scoreboard) {
        final String SIDEBAR_OBJECTIVE_NAME = "sidebar";

        final Component heading = Component.text("ZOMBIES").decorate(TextDecoration.BOLD).color(NamedTextColor.YELLOW);
        final Objective sidebarObjective = scoreboard.registerNewObjective(SIDEBAR_OBJECTIVE_NAME, Criteria.DUMMY, heading);
        sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        final List<Component> content = getSidebarContent(player);
        for (int i = 0; i < content.size(); i++) {
            final Component component = content.get(i);
            final int scoreNumber = content.size() - i;
            final Score score = sidebarObjective.getScore(Integer.toString(i));
            score.customName(component);
            score.setScore(scoreNumber);
            score.numberFormat(NumberFormat.blank());
        }
    }

    private static void updateKills(final ZombiesPlayer player, final Scoreboard scoreboard) {
        final String KILLS_OBJECTIVE_NAME = "kills";

        final Objective killsObjective = scoreboard.registerNewObjective(KILLS_OBJECTIVE_NAME, Criteria.DUMMY, Component.text("Kills"));
        killsObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);

        final ZombiesWorld world = player.getWorld();
        for (final ZombiesPlayer otherPlayer : world.getPlayers()) {
            final int kills = otherPlayer.get(ZombiesPlayer.KILLS);
            final Score score = killsObjective.getScore(otherPlayer.getBukkit());
            score.setScore(kills);
        }
    }

    private static void updateHealth(final ZombiesPlayer player, final Scoreboard scoreboard) {
        final String HEALTH_OBJECTIVE_NAME = "health";

        final Objective healthObjective = scoreboard.registerNewObjective(HEALTH_OBJECTIVE_NAME, Criteria.DUMMY, Component.text("Health"));
        healthObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        healthObjective.setRenderType(RenderType.HEARTS);
        healthObjective.setAutoUpdateDisplay(true);
        final ZombiesWorld world = player.getWorld();
        for (final ZombiesPlayer otherPlayer : world.getPlayers()) {
            final double health = otherPlayer.getBukkit().getHealth();
            final Score score = healthObjective.getScore(otherPlayer.getBukkit());
            score.setScore((int) health);
        }
    }

    public static void updateScoreboard(final ZombiesPlayer player) {
        if (!player.getWorld().isGameRunning()) {
            return;
        }
        final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        final Player bukkit = player.getBukkit();
        final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        bukkit.setScoreboard(scoreboard);
        updateSidebar(player, scoreboard);
        updateKills(player, scoreboard);
        updateHealth(player, scoreboard);
    }

    private static void updateScoreboard(final ZombiesWorld world) {
        for (final ZombiesPlayer player : world.getPlayers()) {
            updateScoreboard(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onGameStart(final GameStartEvent event) {
        updateScoreboard(event.getWorld());
    }

    @EventHandler
    private void onPlayerJoins(final PlayerJoinEvent event) {
        updateScoreboard(new ZombiesPlayer(event.getPlayer()));
    }

    @EventHandler
    private void onPlayerKillsIncrement(final PlayerKillsIncrementEvent event) {
        updateScoreboard(event.getWorld());
    }

    @EventHandler
    private void onPlayerGoldChangeEvent(final PlayerGoldChangeEvent event) {
        updateScoreboard(event.getWorld());
    }

    @EventHandler
    private void onPlayerKillsZombie(final PlayerKillZombieEvent event) {
        updateScoreboard(event.getWorld());
    }

    @EventHandler
    private void onRoundStart(final StartRoundEvent event) {
        updateScoreboard(event.getWorld());
    }

    private void onHealthChange(final EntityEvent event) {
        if (!(event.getEntity() instanceof final Player player)) {
            return;
        }
        final ZombiesPlayer zombiesPlayer = new ZombiesPlayer(player);
        final ZombiesWorld world = zombiesPlayer.getWorld();
        if (!world.isGameRunning()) {
            return;
        }
        Bukkit.getScheduler().runTask(ZombiesPlugin.INSTANCE, () -> updateScoreboard(world));
    }

    @EventHandler
    private void onEntityDamage(final EntityDamageEvent event) {
        onHealthChange(event);
    }

    @EventHandler
    private void onEntityRegainHealth(final EntityRegainHealthEvent event) {
        onHealthChange(event);
    }

    @EventHandler
    private void onGameEnd(final GameEndEvent event) {
        for (final ZombiesPlayer player : event.getWorld().getPlayers()) {
            player.getBukkit().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }
}
