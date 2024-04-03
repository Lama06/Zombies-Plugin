package io.lama06.zombies.event.perk;

import io.lama06.zombies.GlobalPerk;
import io.lama06.zombies.event.ZombiesEvent;
import io.lama06.zombies.player.ZombiesPlayer;
import io.lama06.zombies.util.HandlerListGetter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerPickupPerkItemEvent extends ZombiesEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final ZombiesPlayer player;
    private final GlobalPerk perk;

    public PlayerPickupPerkItemEvent(final ZombiesPlayer player, final GlobalPerk perk) {
        super(player.getWorld());
        this.player = player;
        this.perk = perk;
    }

    public ZombiesPlayer getPlayer() {
        return player;
    }

    public GlobalPerk getPerk() {
        return perk;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
