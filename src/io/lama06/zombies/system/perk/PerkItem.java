package io.lama06.zombies.system.perk;

import io.lama06.zombies.ZombiesPlugin;
import org.bukkit.NamespacedKey;

final class PerkItem {
    static NamespacedKey getRemainingTimeKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, "remaining_time");
    }

    static NamespacedKey getPerkNameKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, "perk_name");
    }
}
