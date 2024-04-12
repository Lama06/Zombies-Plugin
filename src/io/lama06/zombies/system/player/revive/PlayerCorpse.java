package io.lama06.zombies.system.player.revive;

import io.lama06.zombies.ZombiesPlugin;
import org.bukkit.NamespacedKey;

public final class PlayerCorpse {
    static final int TIME = 15 * 20;
    static final int RELIVE_TIME = 3 * 20;

    static NamespacedKey getPlayerKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, "player");
    }

    static NamespacedKey getRemainingTimeKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, "remaining_time");
    }

    static NamespacedKey getReliveTimeKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, "relive_time");
    }
}
