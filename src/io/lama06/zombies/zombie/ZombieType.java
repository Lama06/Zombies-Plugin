package io.lama06.zombies.zombie;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public enum ZombieType {
    NORMAL(
            ZombieData.builder()
                    .setEntity(EntityType.ZOMBIE)
                    .addEquipment(EquipmentSlot.CHEST, new ItemStack(Material.LEATHER_CHESTPLATE))
                    .setHealth(10)
    );

    public final ZombieData data;

    ZombieType(final ZombieData.Builder data) {
        this.data = data.build();
    }
}
