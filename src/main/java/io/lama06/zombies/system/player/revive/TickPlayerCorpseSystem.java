package io.lama06.zombies.system.player.revive;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.perk.PlayerPerk;
import io.lama06.zombies.util.pdc.UuidDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class TickPlayerCorpseSystem implements Listener {
    private static final double REVIVE_RADIUS = 4;

    @EventHandler
    private void onTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getWorlds()) {
            final Collection<ArmorStand> armorStands = world.getBukkit().getEntitiesByClass(ArmorStand.class);
            for (final ArmorStand corpse : armorStands) {
                final PersistentDataContainer pdc = corpse.getPersistentDataContainer();
                final UUID playerUuid = pdc.get(PlayerCorpse.getPlayerKey(), UuidDataType.INSTANCE);
                if (playerUuid == null) {
                    continue;
                }
                final Player corpsePlayer = Bukkit.getPlayer(playerUuid);
                if (!world.isGameRunning() || corpsePlayer == null || !corpsePlayer.getWorld().equals(world.getBukkit())) {
                    corpse.remove();
                    continue;
                }
                final Integer remainingTime = pdc.get(PlayerCorpse.getRemainingTimeKey(), PersistentDataType.INTEGER);
                final Integer reliveTime = pdc.get(PlayerCorpse.getReliveTimeKey(), PersistentDataType.INTEGER);
                if (remainingTime == null || remainingTime <= 0 || reliveTime == null) {
                    corpse.remove();
                    continue;
                }
                if (reliveTime <= 0) {
                    corpse.remove();
                    corpsePlayer.setGameMode(GameMode.ADVENTURE);
                    corpsePlayer.teleport(corpse.getLocation());
                    corpsePlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6 * 20, 2));
                    world.sendMessage(corpsePlayer.displayName().append(Component.text(" has been revived").color(NamedTextColor.GREEN)));
                    continue;
                }
                final List<Player> nearbyPlayers = corpse.getNearbyEntities(REVIVE_RADIUS, REVIVE_RADIUS, REVIVE_RADIUS)
                        .stream().filter(e -> e instanceof Player).map(e -> (Player) e).toList();
                boolean reviving = false;
                for (final Player nearbyPlayer : nearbyPlayers) {
                    nearbyPlayer.sendActionBar(Component.text("Sneak to revive: %.1fs".formatted(reliveTime / 20f)).color(NamedTextColor.YELLOW));
                    if (nearbyPlayer.isSneaking()) {
                        reviving = true;
                        final int reviveSpeed = new ZombiesPlayer(nearbyPlayer).hasPerk(PlayerPerk.FAST_REVIVE) ? 3 : 1;
                        pdc.set(PlayerCorpse.getReliveTimeKey(), PersistentDataType.INTEGER, reliveTime - reviveSpeed);
                    }
                }
                if (!reviving) {
                    pdc.set(PlayerCorpse.getRemainingTimeKey(), PersistentDataType.INTEGER, remainingTime - 1);
                }
            }
        }
    }
}
