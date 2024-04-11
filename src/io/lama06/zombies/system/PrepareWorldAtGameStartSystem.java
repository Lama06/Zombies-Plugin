package io.lama06.zombies.system;

import io.lama06.zombies.*;
import io.lama06.zombies.data.Component;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.perk.GlobalPerk;
import io.lama06.zombies.weapon.WeaponType;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public final class PrepareWorldAtGameStartSystem implements Listener {
    @EventHandler
    private void onGameStart(final GameStartEvent event) {
        final ZombiesWorld world = event.getWorld();
        final WorldConfig config = world.getConfig();
        final SpawnRate firstRoundSpawnRate = SpawnRate.SPAWN_RATES.get(0);
        final Integer lastGameId = world.get(ZombiesWorld.GAME_ID);
        final int gameId = lastGameId == null ? 1 : lastGameId + 1;

        world.getBukkit().setGameRule(GameRule.DO_FIRE_TICK, false);
        world.getBukkit().setGameRule(GameRule.MOB_GRIEFING, false);
        world.getBukkit().setGameRule(GameRule.DISABLE_RAIDS, true);

        world.set(ZombiesWorld.GAME_ID, gameId);
        world.set(ZombiesWorld.ROUND, 1);
        world.set(ZombiesWorld.OPEN_DOORS, List.of());
        world.set(ZombiesWorld.REACHABLE_AREAS, List.of(config.startArea));
        world.set(ZombiesWorld.POWER_SWITCH, false);
        world.set(ZombiesWorld.NEXT_ZOMBIE_TIME, firstRoundSpawnRate.spawnDelay());
        world.set(ZombiesWorld.REMAINING_ZOMBIES, firstRoundSpawnRate.getNumberOfZombies());
        world.set(ZombiesWorld.BOSS_SPAWNED, false);
        world.set(ZombiesWorld.DRAGONS_WRATH_USED, 0);

        final Component perksComponent = world.addComponent(ZombiesWorld.PERKS_COMPONENT);
        for (final GlobalPerk perk : GlobalPerk.values()) {
            perksComponent.set(perk.getRemainingTimeAttribute(), 0);
        }

        for (final Window window : config.windows) {
            window.close(event.getWorld());
        }
        for (final Door door : config.doors) {
            door.setOpen(world, false);
        }
        if (config.powerSwitch != null) {
            config.powerSwitch.setActive(world, false);
        }

        for (final ZombiesPlayer player : world.getPlayers()) {
            player.set(ZombiesPlayer.GAME_ID, gameId);
            player.set(ZombiesPlayer.KILLS, 0);
            player.set(ZombiesPlayer.GOLD, 0);
            final Player bukkit = player.getBukkit();
            bukkit.getInventory().clear();
            bukkit.teleport(world.getBukkit().getSpawnLocation());
            bukkit.setFoodLevel(20);
            bukkit.setHealth(20);
            bukkit.setGameMode(GameMode.ADVENTURE);
            bukkit.setLevel(0);
            bukkit.setExp(0);
            bukkit.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
            bukkit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 5));
            player.giveWeapon(0, WeaponType.KNIFE);
            player.giveWeapon(1, WeaponType.PISTOL);
        }
    }
}
