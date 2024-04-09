package io.lama06.zombies.event.perk;

import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.event.ZombiesEvent;
import io.lama06.zombies.perk.GlobalPerk;
import io.lama06.zombies.util.HandlerListGetter;
import org.bukkit.event.HandlerList;

public final class PerkTickEvent extends ZombiesEvent {
    public static final HandlerList HANDLERS = new HandlerList();

    @HandlerListGetter
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final GlobalPerk perk;
    private final int remainingTime;

    public PerkTickEvent(final ZombiesWorld world, final GlobalPerk perk, final int remainingTime) {
        super(world);
        this.perk = perk;
        this.remainingTime = remainingTime;
    }

    public GlobalPerk getPerk() {
        return perk;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
