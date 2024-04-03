package io.lama06.zombies.system.perk;

import io.lama06.zombies.GlobalPerk;
import io.lama06.zombies.zombie.Zombie;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public final class SpawnPerkItemsOnZombieDeathSystem implements Listener {
    private static final int PERK_ITEM_TIME = 10 * 20;
    private static final double PERK_PROBABILITY = 0.05;

    @EventHandler
    private void onEntityDeath(final EntityDeathEvent event) {
        final Zombie zombie = new Zombie(event.getEntity());
        if (!zombie.isZombie()) {
            return;
        }
        final RandomGenerator rnd = ThreadLocalRandom.current();
        if (rnd.nextDouble() >= PERK_PROBABILITY) {
            return;
        }
        final GlobalPerk[] perks = GlobalPerk.values();
        final GlobalPerk perk = perks[rnd.nextInt(perks.length)];
        final World world = event.getEntity().getWorld();
        final ItemDisplay display = world.spawn(zombie.getEntity().getLocation().clone().add(0, 1, 0), ItemDisplay.class);
        display.setCustomNameVisible(true);
        display.customName(perk.getDisplayName());
        display.setItemStack(new ItemStack(perk.getMaterial()));
        final PersistentDataContainer pdc = display.getPersistentDataContainer();
        pdc.set(PerkItem.getPerkNameKey(), PersistentDataType.STRING, perk.name());
        pdc.set(PerkItem.getRemainingTimeKey(), PersistentDataType.INTEGER, PERK_ITEM_TIME);
    }
}
