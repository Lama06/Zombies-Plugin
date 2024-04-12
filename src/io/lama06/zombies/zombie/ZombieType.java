package io.lama06.zombies.zombie;

import io.lama06.zombies.util.PlayerHeads;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public enum ZombieType {
    NORMAL_EASY(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .setBreakWindow(new BreakWindowData(40))
                    .setHealth(10)
    ),
    NORMAL_MEDIUM(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .addEquipment(EquipmentSlot.CHEST, new ItemStack(Material.LEATHER_CHESTPLATE))
                    .addEquipment(EquipmentSlot.FEET, new ItemStack(Material.LEATHER_BOOTS))
                    .setBreakWindow(new BreakWindowData(30))
                    .setHealth(12)
    ),
    NORMAL_HARD(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .addEquipment(EquipmentSlot.CHEST, new ItemStack(Material.IRON_CHESTPLATE))
                    .addEquipment(EquipmentSlot.LEGS, new ItemStack(Material.IRON_LEGGINGS))
                    .addEquipment(EquipmentSlot.HAND, new ItemStack(Material.DIAMOND_AXE))
                    .setBreakWindow(new BreakWindowData(20))
                    .setHealth(15)
    ),
    PIG_ZOMBIE(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIFIED_PIGLIN)
                    .setHealth(10)
                    .setBreakWindow(new BreakWindowData(20))
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
                    .setBreakWindow(new BreakWindowData(20))
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
                    .setBreakWindow(new BreakWindowData(20))
                    .setHealth(10)
                    .setExplosionAttack(ExplosionAttackData.explodeOnDeath(4))
                    .addEquipment(EquipmentSlot.HEAD, new ItemStack(Material.TNT))
                    .setInitializer(entity -> ((Zombie) entity).setBaby())
    ),
    FIRE_ZOMBIE(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .setHealth(10)
                    .setBreakWindow(new BreakWindowData(20))
                    .setFireImmune(true)
                    .setFireAttack(new FireAttackData(3*20))
                    .addEquipment(EquipmentSlot.HAND, new ItemStack(Material.BLAZE_ROD))
    ),
    ZOMBIE_WOLF(
            new ZombieData()
                    .setEntity(EntityType.WOLF)
                    .setHealth(10)
                    .setBreakWindow(new BreakWindowData(20))
                    .setInitializer(entity -> ((Wolf) entity).setAngry(true))
    ),
    GUARDIAN_ZOMBIE(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .setHealth(13)
                    .setBreakWindow(new BreakWindowData(2*20))
                    .addEquipment(EquipmentSlot.HEAD, new ItemStack(Material.SEA_LANTERN))
                    .setLaserAttack(new LaserAttackData(3))
    ),
    INFERNO(
            new ZombieData()
                    .setEntity(EntityType.ZOMBIE)
                    .setHealth(100)
                    .addEquipment(EquipmentSlot.HAND, new ItemStack(Material.BLAZE_ROD))
                    .setFireTrail(true)
                    .setBreakWindow(new BreakWindowData(20))
                    .setFireImmune(true)
                    .setFireBallAttack(new FireBallAttackData(4, 40))
                    .setFireAttack(new FireAttackData(40))
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
