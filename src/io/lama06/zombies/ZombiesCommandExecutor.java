package io.lama06.zombies;

import com.google.gson.JsonParseException;
import io.lama06.zombies.event.player.PlayerGoldChangeEvent;
import io.lama06.zombies.weapon.WeaponType;
import io.lama06.zombies.zombie.ZombieType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public final class ZombiesCommandExecutor implements TabExecutor {
    private static final String DEAD_END_TEMPLATE = "templates/dead_end.json";

    @Override
    public boolean onCommand(
            @NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label,
            final @NotNull String[] args
    ) {
        if (args.length == 0) {
            root(sender);
            return true;
        }

        switch (args[0]) {
            case "config" -> config(sender);
            case "saveConfig" -> saveConfig(sender);
            case "checkConfig" -> checkConfig(sender);
            case "loadTemplate" -> loadTemplate(sender);
            case "start" -> start(sender);
            case "stop" -> stop(sender);
            case "giveGold" -> giveGold(sender, Arrays.copyOfRange(args, 1, args.length));
            case "giveWeapon" -> giveWeapon(sender, Arrays.copyOfRange(args, 1, args.length));
            case "spawnZombie" -> spawnZombie(sender, Arrays.copyOfRange(args, 1, args.length));
            default -> sender.sendMessage(Component.text("unknown command").color(NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(
            @NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label,
            final @NotNull String[] args
    ) {
        if (args.length != 1 && args.length != 0) {
            return List.of();
        }
        return List.of(
                "config",
                "saveConfig",
                "checkConfig",
                "loadTemplate",
                "start",
                "stop",
                "giveGold",
                "giveWeapon",
                "spawnZombie"
        );
    }

    private void root(final CommandSender sender) {
        final Component equalSigns = Component.text("=".repeat(10)).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD);
        final Component obfuscatedChar = Component.text("_").decorate(TextDecoration.OBFUSCATED);
        final TextComponent.Builder builder = Component.text();

        builder.append(equalSigns).append(obfuscatedChar).appendSpace();
        builder.append(Component.text("Zombies-Plugin").color(NamedTextColor.LIGHT_PURPLE));
        builder.appendSpace().append(obfuscatedChar).append(equalSigns);

        builder.appendNewline();
        builder.append(Component.text("Creator: "));
        builder.append(Component.text("Lama06").color(NamedTextColor.GOLD));

        builder.appendNewline();
        builder.append(Component.text("Website: "));
        builder.append(Component.text("github.com/Lama06/Zombies-Plugin")
                               .clickEvent(ClickEvent.openUrl("https://github.com/Lama06/Zombies-Plugin/")));

        sender.sendMessage(builder);
    }

    private void config(final CommandSender sender) {
        if (!(sender instanceof final Player player)) {
            return;
        }
        final ZombiesWorld world = new ZombiesWorld(player.getWorld());
        WorldConfig config = world.getConfig();
        if (config == null) {
            final ZombiesConfig globalConfig = ZombiesPlugin.INSTANCE.getGlobalConfig();
            config = new WorldConfig();
            globalConfig.worlds.put(world.getBukkit().getName(), config);
        }
        config.openMenu(player, () -> {});
    }

    private void saveConfig(final CommandSender sender) {
        try {
            ZombiesPlugin.INSTANCE.saveZombiesConfig();
        } catch (final IOException e) {
            sender.sendMessage(Component.text("error: " + e.getMessage()));
            ZombiesPlugin.INSTANCE.getSLF4JLogger().error("failed to save config", e);
        }
        sender.sendMessage(Component.text("Saved").color(NamedTextColor.GREEN));
    }

    private void checkConfig(final CommandSender sender) {
        if (!(sender instanceof final Player player)) {
            return;
        }
        final WorldConfig config = ZombiesPlugin.INSTANCE.getWorldConfig(new ZombiesWorld(player.getWorld()));
        if (config == null) {
            sender.sendMessage(Component.text("This world isn't configured"));
            return;
        }
        try {
            config.check();
        } catch (final InvalidConfigException e) {
            sender.sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
            return;
        }
        sender.sendMessage(Component.text("No issues found").color(NamedTextColor.GREEN));
    }

    private void loadTemplate(final CommandSender sender) {
        if (!(sender instanceof final Player player)) {
            return;
        }
        final ZombiesWorld world = new ZombiesWorld(player.getWorld());
        final ZombiesConfig globalConfig = ZombiesPlugin.INSTANCE.getGlobalConfig();
        final InputStream resource = ZombiesPlugin.INSTANCE.getResource(DEAD_END_TEMPLATE);
        if (resource == null) {
            sender.sendMessage(Component.text("Template unavailable. Please contact the plugins's author.").color(NamedTextColor.RED));
            return;
        }
        final WorldConfig config;
        try {
            config = ConfigManager.createGson().fromJson(new InputStreamReader(resource), WorldConfig.class);
        } catch (final JsonParseException e) {
            sender.sendMessage(Component.text("Template malformed. Please contact the plugins's author."));
            return;
        }
        globalConfig.worlds.put(world.getBukkit().getName(), config);
        sender.sendMessage(Component.text("Template loaded. Start the game: Click me!").color(NamedTextColor.GREEN)
                                   .clickEvent(ClickEvent.suggestCommand("/zombies start")));
    }

    private void start(final CommandSender sender) {
        if (!(sender instanceof final Player player)) {
            return;
        }
        final ZombiesWorld world = new ZombiesWorld(player.getWorld());
        try {
            world.getConfig().check();
        } catch (final InvalidConfigException e) {
            player.sendMessage(Component.text(e.getMessage()));
            return;
        }
        world.startGame();
    }

    private void stop(final CommandSender sender) {
        if (!(sender instanceof final Player player)) {
            return;
        }
        final ZombiesWorld world = new ZombiesWorld(player.getWorld());
        world.endGame();
    }

    private void giveWeapon(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            return;
        }
        final WeaponType weaponType;
        try {
            weaponType = WeaponType.valueOf(args[0].toUpperCase());
        } catch (final IllegalArgumentException e) {
            return;
        }
        if (!(sender instanceof final Player player)) {
            return;
        }
        final ZombiesPlayer zombiesPlayer = new ZombiesPlayer(player);
        zombiesPlayer.giveWeapon(player.getInventory().getHeldItemSlot(), weaponType);
    }

    private void giveGold(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) {
            return;
        }
        final ZombiesPlayer zombiesPlayer = new ZombiesPlayer(player);
        if (args.length == 0) {
            return;
        }
        final int goldAdd;
        try {
            goldAdd = Integer.parseInt(args[0]);
        } catch (final NumberFormatException e) {
            return;
        }
        final int goldPrevious = zombiesPlayer.get(ZombiesPlayer.GOLD);
        final int golfAfter = goldPrevious + goldAdd;
        zombiesPlayer.set(ZombiesPlayer.GOLD, golfAfter);
        Bukkit.getPluginManager().callEvent(new PlayerGoldChangeEvent(zombiesPlayer, goldPrevious, golfAfter));
    }

    private void spawnZombie(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) {
            return;
        }
        final ZombiesWorld world = new ZombiesWorld(player.getWorld());
        if (!world.isGameRunning()) {
            return;
        }
        if (args.length == 0) {
            return;
        }
        final ZombieType zombieType;
        try {
            zombieType = ZombieType.valueOf(args[0].toUpperCase());
        } catch (final IllegalArgumentException e) {
            return;
        }
        world.spawnZombie(player.getLocation(), zombieType);
    }
}
