package io.lama06.zombies.zombie.system;

import io.lama06.zombies.System;
import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.event.ZombieSpawnEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class ApplyEquipmentSystem extends System {
    public ApplyEquipmentSystem(final ZombiesGame game) {
        super(game);
    }

    @EventHandler
    private void handleZombieSpawn(final ZombieSpawnEvent event) {
        if (!event.getGame().equals(game) || event.getZombie().getEquipment() == null) {
            return;
        }
        for (final Map.Entry<EquipmentSlot, ItemStack> entry : event.getZombie().getEquipment().getEquipment().entrySet()) {
            if (!(event.getZombie().getEntity() instanceof final LivingEntity livingEntity)) {
                continue;
            }
            if (livingEntity.getEquipment() == null) {
                continue;
            }
            livingEntity.getEquipment().setItem(entry.getKey(), entry.getValue(), true);
        }
    }
}
