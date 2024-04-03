package io.lama06.zombies.zombie;

import io.lama06.zombies.util.PlayerHeads;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public enum ZombieType {
    NORMAL_EASY(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .setBreakWindow(new BreakWindowData(3*20))
                    .setHealth(10)
    ),
    NORMAL_MEDIUM(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .addEquipment(EquipmentSlot.CHEST, new ItemStack(Material.LEATHER_CHESTPLATE))
                    .addEquipment(EquipmentSlot.FEET, new ItemStack(Material.LEATHER_BOOTS))
                    .setBreakWindow(new BreakWindowData(2*20 + 10))
                    .setHealth(12)
    ),
    NORMAL_HARD(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .addEquipment(EquipmentSlot.CHEST, new ItemStack(Material.IRON_CHESTPLATE))
                    .addEquipment(EquipmentSlot.LEGS, new ItemStack(Material.IRON_LEGGINGS))
                    .addEquipment(EquipmentSlot.HAND, new ItemStack(Material.DIAMOND_AXE))
                    .setBreakWindow(new BreakWindowData(2*20))
                    .setHealth(15)
    ),
    PIG_ZOMBIE(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIFIED_PIGLIN)
                    .setHealth(10)
                    .setBreakWindow(new BreakWindowData(3*20))
                    .setFireImmune(true)
    ),
    MAGMA_CUBE(
            new ZombieData()
                    .setEntity(EntityType.MAGMA_CUBE)
                    .setHealth(3)
                    .setFireImmune(true)
    ),
    MAGMA_ZOMBIE(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .setBreakWindow(new BreakWindowData(3*20))
                    .setHealth(7)
                    .setFireImmune(true)
                    .setDescendants(new DescendantsData(ZombieType.MAGMA_CUBE, 3))
                    .addEquipment(EquipmentSlot.HAND, new ItemStack(Material.GOLDEN_SWORD))
                    .addEquipment(EquipmentSlot.HEAD, PlayerHeads.MAGMA_CUBE.createItem())
                    .addEquipment(EquipmentSlot.CHEST, new ItemStack(Material.GOLDEN_CHESTPLATE))
                    .addEquipment(EquipmentSlot.LEGS, new ItemStack(Material.GOLDEN_LEGGINGS))
                    .addEquipment(EquipmentSlot.FEET, new ItemStack(Material.GOLDEN_BOOTS))
    ),
    LITTLE_BOMBIE(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .setBreakWindow(new BreakWindowData(3*20))
                    .setHealth(10)
                    .setExplosionAttack(ExplosionAttackData.explodeOnDeath(4))
                    .addEquipment(EquipmentSlot.HAND, new ItemStack(Material.TNT))
    ),
    FIRE_ZOMBIE(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .setHealth(10)
                    .setFireImmune(true)
                    .setFireAttack(new FireAttackData(3*20))
    ),
    BOMBIE(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .setHealth(100)
                    .setExplosionAttack(ExplosionAttackData.explodePeriodically(2*20 + 10, 4))
                    .setBreakWindow(new BreakWindowData(20))
    );

    public final ZombieData data;

    ZombieType(final ZombieData data) {
        this.data = data;
    }
}
