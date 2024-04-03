package io.lama06.zombies;

import io.lama06.zombies.data.Storage;
import io.lama06.zombies.data.StorageSession;
import io.lama06.zombies.event.GameEndEvent;
import io.lama06.zombies.event.GameStartEvent;
import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.ZombieAttributes;
import io.lama06.zombies.zombie.ZombieType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public final class ZombiesWorld extends Storage implements ForwardingAudience {
    private final World world;

    public ZombiesWorld(final World world) {
        this.world = world;
    }

    public boolean isGameRunning() {
        return getOrDefault(WorldAttributes.GAME_RUNNING, false);
    }

    public boolean isZombiesWorld() {
        return ZombiesPlugin.INSTANCE.isZombiesWorld(this);
    }

    public WorldConfig getConfig() {
        return ZombiesPlugin.INSTANCE.getConfig(this);
    }

    public void startGame() {
        set(WorldAttributes.GAME_RUNNING, true);
        Bukkit.getPluginManager().callEvent(new GameStartEvent(this));
    }

    public void endGame() {
        set(WorldAttributes.GAME_RUNNING, false);
        Bukkit.getPluginManager().callEvent(new GameEndEvent(this));
    }

    public List<Zombie> getZombies() {
        return world.getEntities().stream().map(Zombie::new).filter(Zombie::isZombie).toList();
    }

    public Zombie spawnZombie(final Location location, final ZombieType type) {
        final Entity entity = world.spawnEntity(location, type.data.entity, false);
        final Zombie zombie = new Zombie(entity);
        zombie.set(ZombieAttributes.IS_ZOMBIE, true);
        zombie.set(ZombieAttributes.TYPE, type);
        Bukkit.getPluginManager().callEvent(new ZombieSpawnEvent(zombie, type.data));
        return zombie;
    }

    public List<ZombiesPlayer> getAlivePlayers() {
        return world.getPlayers().stream()
                .filter(player -> player.getGameMode() == GameMode.ADVENTURE)
                .map(ZombiesPlayer::new)
                .toList();
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
