package io.lama06.zombies.system;

import io.lama06.zombies.WorldAttributes;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.player.PlayerAttributes;
import io.lama06.zombies.player.ZombiesPlayer;
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
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public final class ScoreboardSystem implements Listener {
    private static List<Component> getSidebarContent(final ZombiesPlayer player) {
        final List<Component> content = new ArrayList<>();
        final ZombiesWorld world = player.getWorld();

        final int round = world.get(WorldAttributes.ROUND);
        content.add(Component.text("Round " + round).color(NamedTextColor.RED));

        final int aliveZombies = world.getZombies().size();
        final int remainingZombies = world.get(WorldAttributes.REMAINING_ZOMBIES);
        final int zombiesLeft = aliveZombies + remainingZombies;
        content.add(Component.text("Zombies Left: ").append(Component.text(zombiesLeft).color(NamedTextColor.GREEN)));

        content.add(Component.empty());

        for (final ZombiesPlayer otherPlayer : world.getPlayers()) {
            final int coins = otherPlayer.get(PlayerAttributes.GOLD);
            final TextComponent.Builder coinsComponent = Component.text();
            coinsComponent.append(Component.text(otherPlayer.getBukkit().getName()));
            coinsComponent.append(Component.text(": "));
            coinsComponent.append(Component.text(coins).color(NamedTextColor.GOLD));
            content.add(coinsComponent.asComponent());
        }

        content.add(Component.empty());

        final int kills = player.get(PlayerAttributes.KILLS);
        content.add(Component.text("Zombie Kills: ").append(Component.text(kills).color(NamedTextColor.GREEN)));

        return content;
    }

    private static void updateSidebar(final ZombiesPlayer player, final Scoreboard scoreboard) {
        final String SIDEBAR_OBJECTIVE_NAME = "sidebar";

        final Objective existingSidebarObjective = scoreboard.getObjective(SIDEBAR_OBJECTIVE_NAME);
        final Objective sidebarObjective;
        if (existingSidebarObjective != null) {
            sidebarObjective = existingSidebarObjective;
        } else {
            final Component heading = Component.text("ZOMBIES").decorate(TextDecoration.BOLD).color(NamedTextColor.YELLOW);
            sidebarObjective = scoreboard.registerNewObjective(SIDEBAR_OBJECTIVE_NAME, Criteria.DUMMY, heading);
            sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

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

    public static void updateScoreboard(final ZombiesPlayer player) {
        final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        final Player bukkit = player.getBukkit();
        final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        bukkit.setScoreboard(scoreboard);
        updateSidebar(player, scoreboard);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void createScoreboardOnGameStart(final GameStartEvent event) {
        for (final ZombiesPlayer player : event.getWorld().getPlayers()) {
            updateScoreboard(player);
        }
    }
}
