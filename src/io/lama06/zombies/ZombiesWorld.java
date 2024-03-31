package io.lama06.zombies;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.weapon.WeaponType;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieData;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ZombiesWorld extends Storage implements ForwardingAudience {
    public static final AttributeId<Boolean> IS_GAME = new AttributeId<>("is_game", PersistentDataType.BOOLEAN);

    public static boolean isGameWorld(final World world) {
        return world.getPersistentDataContainer().getOrDefault(IS_GAME.getKey(), PersistentDataType.BOOLEAN, false);
    }

    private final World world;

    public ZombiesWorld(final World world) {
        this.world = world;
    }

    public void startGame() {
        final WorldConfig config = ZombiesPlugin.getConfig(this);
        if (config == null) {
            return;
        }
        for (final ZombiesPlayer player :getPlayers()) {
            final Player bukkit = player.getBukkit();
            bukkit.getInventory().clear();
            bukkit.setFoodLevel(20);
            bukkit.setHealth(20);
            player.giveWeapon(0, WeaponType.KNIFE.data);
            player.giveWeapon(1, WeaponType.PISTOL.data);
        }
        final PersistentDataContainer pdc = world.getPersistentDataContainer();
        pdc.set(IS_GAME.getKey(), PersistentDataType.BOOLEAN, true);
        Bukkit.getPluginManager().callEvent(new GameStartEvent(this));
    }

    public List<Zombie> getZombies() {
        final List<Zombie> zombies = new ArrayList<>();
        for (final Entity entity : world.getEntities()) {
            if (!Zombie.isZombie(entity)) {
                continue;
            }
            zombies.add(new Zombie(entity));
        }
        return zombies;
    }

    public Zombie spawnZombie(final Location location, final ZombieData data) {
        final Entity entity = world.spawnEntity(location, data.entity(), false);
        final PersistentDataContainer pdc = entity.getPersistentDataContainer();
        pdc.set(Zombie.IS_ZOMBIE.getKey(), PersistentDataType.BOOLEAN, true);
        final Zombie zombie = new Zombie(entity);
        Bukkit.getPluginManager().callEvent(new ZombieSpawnEvent(zombie, data));
        return zombie;
    }

    public List<ZombiesPlayer> getPlayers() {
        return world.getPlayers().stream().map(ZombiesPlayer::new).toList();
    }

    @Override
    protected StorageSession startSession() {
        return world::getPersistentDataContainer;
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return Set.of(world);
    }

    public World getBukkit() {
        return world;
    }
}
