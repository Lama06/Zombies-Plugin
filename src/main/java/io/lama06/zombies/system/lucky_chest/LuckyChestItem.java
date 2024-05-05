package io.lama06.zombies.system.lucky_chest;

import io.lama06.zombies.ZombiesPlugin;
import org.bukkit.NamespacedKey;

final class LuckyChestItem {
    static final int SHUFFLE_TIME = 7 * 20;
    static final int SHUFFLE_DELAY = 20;

    static NamespacedKey getRemainingTimeKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, "lucky_chest_remaining_time");
    }

    static NamespacedKey getWeaponKey() {
        return new NamespacedKey(ZombiesPlugin.INSTANCE, "lucky_chest_weapon");
    }
}
