package io.lama06.zombies.zombie.break_window;

import io.lama06.zombies.zombie.Zombie;
import io.lama06.zombies.zombie.break_window.event.BreakWindowCompleteEvent;
import io.lama06.zombies.zombie.break_window.event.BreakWindowStartEvent;
import io.lama06.zombies.zombie.break_window.event.BreakWindowTickEvent;
import io.lama06.zombies.zombie.component.ZombieComponent;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.Bukkit;

public final class BreakWindowComponent extends ZombieComponent {
    private final int time;
    private final double maxDistance;

    private int remainingTime;
    private BlockPosition windowBlock;

    public BreakWindowComponent(final Zombie zombie, final BreakWindowData data) {
        super(zombie);
        time = data.time();
        maxDistance = data.maxDistance();

        windowBlock = null;
        remainingTime = 0;
    }

    public void start(final BlockPosition windowBlock) {
        if (isBreakingWindow()) {
            throw new IllegalStateException("already breaking window");
        }
        this.windowBlock = windowBlock;
        remainingTime = time;
        Bukkit.getPluginManager().callEvent(new BreakWindowStartEvent(zombie));
    }

    public void tick() {
        if (!isBreakingWindow()) {
            throw new IllegalStateException("not breaking window");
        }
        if (!new BreakWindowTickEvent(zombie, remainingTime-1).callEvent()) {
            cancel();
            return;
        }
        remainingTime--;
        if (remainingTime == 0) {
            Bukkit.getPluginManager().callEvent(new BreakWindowCompleteEvent(zombie));
            windowBlock = null;
        }
    }

    public void cancel() {
        if (!isBreakingWindow()) {
            throw new IllegalStateException("not breaking window");
        }
        windowBlock = null;
        remainingTime = 0;
    }

    public boolean isBreakingWindow() {
        return windowBlock != null;
    }

    public int getTime() {
        return time;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public BlockPosition getWindowBlock() {
        return windowBlock;
    }
}
