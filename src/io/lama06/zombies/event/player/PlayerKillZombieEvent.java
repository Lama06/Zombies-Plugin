package io.lama06.zombies.event.player;

import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.util.HandlerListGetter;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerKillZombieEvent extends PlayerEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final Zombie zombie;

    public PlayerKillZombieEvent(final ZombiesPlayer player, final Zombie zombie) {
        super(player);
        this.zombie = zombie;
    }

    public Zombie getZombie() {
        return zombie;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
