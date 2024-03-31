package io.lama06.zombies.zombie;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public enum ZombieType {
    NORMAL_EASY(
            ZombieData.builder()
                    .setEntity(EntityType.ZOMBIE)
                    .setBreakWindow(new BreakWindowData(5*20, 3))
                    .setHealth(10)
    ),
    NORMAL_MEDIUM(
            ZombieData.builder()
                    .setEntity(EntityType.ZOMBIE)
                    .addEquipment(EquipmentSlot.CHEST, new ItemStack(Material.LEATHER_CHESTPLATE))
                    .addEquipment(EquipmentSlot.FEET, new ItemStack(Material.LEATHER_BOOTS))
                    .setBreakWindow(new BreakWindowData(4*20, 3))
                    .setHealth(20)
    ),
    NORMAL_HARD(
            ZombieData.builder()
                    .setEntity(EntityType.ZOMBIE)
                    .addEquipment(EquipmentSlot.CHEST, new ItemStack(Material.IRON_CHESTPLATE))
                    .addEquipment(EquipmentSlot.LEGS, new ItemStack(Material.IRON_LEGGINGS))
                    .addEquipment(EquipmentSlot.HAND, new ItemStack(Material.DIAMOND_AXE))
                    .setBreakWindow(new BreakWindowData(3*20, 3))
                    .setHealth(20)
    );

    public final ZombieData data;

    ZombieType(final ZombieData.Builder data) {
        this.data = data.build();
    }
}
