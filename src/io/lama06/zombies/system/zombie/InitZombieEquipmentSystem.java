package io.lama06.zombies.system.zombie;

import io.lama06.zombies.event.zombie.ZombieSpawnEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class InitZombieEquipmentSystem implements Listener {
    @EventHandler
    private void onSpawn(final ZombieSpawnEvent event) {
        final Entity entity = event.getZombie().getEntity();
        if (!(entity instanceof final LivingEntity living)) {
            return;
        }
        final EntityEquipment equipment = living.getEquipment();
        if (equipment == null) {
            return;
        }
        final Map<EquipmentSlot, ItemStack> newEquipment = event.getData().equipment();
        for (final EquipmentSlot slot : newEquipment.keySet()) {
            equipment.setItem(slot, newEquipment.get(slot));
        }
    }
}
