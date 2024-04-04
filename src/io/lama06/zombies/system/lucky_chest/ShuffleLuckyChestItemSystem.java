package io.lama06.zombies.system.lucky_chest;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.lama06.zombies.ZombiesPlugin;
import io.lama06.zombies.ZombiesWorld;
import io.lama06.zombies.util.pdc.EnumPersistentDataType;
import io.lama06.zombies.weapon.WeaponType;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public final class ShuffleLuckyChestItemSystem implements Listener {
    @EventHandler
    private void onServerTick(final ServerTickEndEvent event) {
        for (final ZombiesWorld world : ZombiesPlugin.INSTANCE.getGameWorlds()) {
            final Collection<ItemDisplay> itemDisplays = world.getBukkit().getEntitiesByClass(ItemDisplay.class);
            for (final ItemDisplay itemDisplay : itemDisplays) {
                final PersistentDataContainer pdc = itemDisplay.getPersistentDataContainer();
                final Integer remainingTime = pdc.get(LuckyChestItem.getRemainingTimeKey(), PersistentDataType.INTEGER);
                if (remainingTime == null || remainingTime == 0) {
                    continue;
                }
                final int passedTime = LuckyChestItem.SHUFFLE_TIME - remainingTime;
                pdc.set(LuckyChestItem.getRemainingTimeKey(), PersistentDataType.INTEGER, remainingTime - 1);
                if (itemDisplay.getItemStack() != null && passedTime % LuckyChestItem.SHUFFLE_DELAY != 0) {
                    continue;
                }
                final List<WeaponType> weaponTypes = Arrays.stream(WeaponType.values())
                        .filter(weaponType -> weaponType.data.inLuckyChest).toList();
                final RandomGenerator rnd = ThreadLocalRandom.current();
                final WeaponType weaponType = weaponTypes.get(rnd.nextInt(weaponTypes.size()));
                pdc.set(LuckyChestItem.getWeaponKey(), new EnumPersistentDataType<>(WeaponType.class), weaponType);
                itemDisplay.setItemStack(new ItemStack(weaponType.getDisplayMaterial()));
                itemDisplay.setCustomNameVisible(true);
                itemDisplay.customName(weaponType.getDisplayName());
                world.getBukkit().playSound(itemDisplay.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 1, 1);
            }
        }
    }
}
