package io.lama06.zombies.system;

import io.lama06.zombies.WorldConfig;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.event.weapon.WeaponAmmoChangeEvent;
import io.lama06.zombies.event.weapon.WeaponClipChangeEvent;
import io.lama06.zombies.menu.SelectionEntry;
import io.lama06.zombies.menu.SelectionMenu;
import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.AmmoData;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.zombie.Zombie;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public final class TeamMachineSystem implements Listener {
    private static final int REFILL_AMMO_PRICE = 1000;
    private static final int FULL_REVIVE_PRICE = 2000;
    private static final int DRAGONS_WRATH_PRICE_BASE = 3000;
    private static final int DRAGONS_WRATH_PRICE_INCREASE = 1000;
    private static final double DRAGONS_WRATH_REACH = 15;

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
            return;
        }
        final ZombiesPlayer player = new ZombiesPlayer(event.getPlayer());
        final ZombiesWorld world = player.getWorld();
        if (!world.isGameRunning()) {
            return;
        }
        final WorldConfig config = world.getConfig();
        if (config.teamMachine == null) {
            return;
        }
        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        if (!clickedBlock.getLocation().toBlock().equals(config.teamMachine)) {
            return;
        }
        SelectionMenu.open(
                player.getBukkit(),
                Component.text("Team Machine").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD),
                () -> {},
                new SelectionEntry(
                        Component.text("Ammo Supply (1000 Gold)").color(NamedTextColor.BLUE),
                        Material.WOODEN_HOE,
                        () -> refillAmmo(player, world)
                ),
                new SelectionEntry(
                        Component.text("Full Revive (2000 Gold)").color(NamedTextColor.GREEN),
                        Material.TOTEM_OF_UNDYING,
                        () -> fullRevive(player, world)
                ),
                new SelectionEntry(
                        Component.text("Dragon's Wrath (%s Gold)".formatted(getDragonsWrathPrice(world)))
                                .color(NamedTextColor.DARK_PURPLE),
                        Material.DRAGON_HEAD,
                        () -> dragonsWrath(player, world)
                )
        );
    }

    private int getDragonsWrathPrice(final ZombiesWorld world) {
        return DRAGONS_WRATH_PRICE_BASE + DRAGONS_WRATH_PRICE_INCREASE * world.get(ZombiesWorld.DRAGONS_WRATH_USED);
    }

    private boolean pay(final ZombiesPlayer player, final int goldPay) {
        final int gold = player.get(ZombiesPlayer.GOLD);
        if (gold < goldPay) {
            player.sendMessage(Component.text("You cannot afford using the team machine").color(NamedTextColor.RED));
            return false;
        }
        final int newGold = gold - goldPay;
        player.set(ZombiesPlayer.GOLD, newGold);
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(player, gold, newGold));
        return true;
    }

    private void refillAmmo(final ZombiesPlayer player, final ZombiesWorld world) {
        if (!pay(player, REFILL_AMMO_PRICE)) {
            return;
        }
        for (final ZombiesPlayer otherPlayer : world.getAlivePlayers()) {
            for (final Weapon weapon : otherPlayer.getWeapons()) {
                final io.lama06.zombies.data.Component ammoComponent = weapon.getComponent(Weapon.AMMO);
                if (ammoComponent == null) {
                    continue;
                }
                final AmmoData ammoData = weapon.getData().ammo;
                final int ammo = ammoComponent.get(AmmoData.AMMO);
                final int clip = ammoComponent.get(AmmoData.CLIP);
                ammoComponent.set(AmmoData.AMMO, ammoData.ammo());
                ammoComponent.set(AmmoData.CLIP, ammoData.clip());
                Bukkit.getPluginManager().callEvent(new WeaponAmmoChangeEvent(weapon, ammo, ammoData.ammo()));
                Bukkit.getPluginManager().callEvent(new WeaponClipChangeEvent(weapon, clip, ammoData.clip()));
            }
        }
    }

    private void fullRevive(final ZombiesPlayer player, final ZombiesWorld world) {
        if (!pay(player, FULL_REVIVE_PRICE)) {
            return;
        }
        for (final ZombiesPlayer deadPlayer : world.getPlayers()) {
            if (deadPlayer.isAlive()) {
                continue;
            }
            final Player deadPlayerBukkit = deadPlayer.getBukkit();
            deadPlayerBukkit.setGameMode(GameMode.ADVENTURE);
            deadPlayerBukkit.setHealth(20);
            deadPlayerBukkit.teleport(world.getBukkit().getSpawnLocation());
        }
    }

    private void dragonsWrath(final ZombiesPlayer player, final ZombiesWorld world) {
        if (!pay(player, getDragonsWrathPrice(world))) {
            return;
        }
        world.set(ZombiesWorld.DRAGONS_WRATH_USED, world.get(ZombiesWorld.DRAGONS_WRATH_USED) + 1);
        final List<Zombie> affectedZombies = world.getZombies().stream()
                .filter(zombie -> zombie.getEntity().getLocation().distance(player.getBukkit().getLocation()) <= DRAGONS_WRATH_REACH)
                .toList();
        for (final Zombie affectedZombie : affectedZombies) {
            if (affectedZombie.getEntity() instanceof final LivingEntity living) {
                living.setKiller(player.getBukkit());
                living.setHealth(0);
            }
        }
    }
}
