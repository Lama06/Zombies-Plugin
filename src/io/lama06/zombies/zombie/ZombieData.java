package io.lama06.zombies.zombie;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class ZombieData {
    public EntityType entity;
    public int health;
    public Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();
    public BreakWindowData breakWindow;
    public boolean fireImmune;
    public LaserAttackData laserAttack;
    public FireAttackData fireAttack;
    public ExplosionAttackData explosionAttack;

    public ZombieData setEntity(final EntityType entity) {
        this.entity = entity;
        return this;
    }

    public ZombieData setHealth(final int health) {
        this.health = health;
        return this;
    }

    public ZombieData addEquipment(final EquipmentSlot slot, final ItemStack item) {
        equipment.put(slot, item);
        return this;
    }

    public ZombieData setBreakWindow(final BreakWindowData breakWindow) {
        this.breakWindow = breakWindow;
        return this;
    }

    public ZombieData setFireImmune(final boolean fireImmune) {
        this.fireImmune = fireImmune;
        return this;
    }

    public ZombieData setLaserAttack(final LaserAttackData laserAttack) {
        this.laserAttack = laserAttack;
        return this;
    }

    public ZombieData setFireAttack(final FireAttackData fireAttack) {
        this.fireAttack = fireAttack;
        return this;
    }

    public ZombieData setExplosionAttack(final ExplosionAttackData explosionAttack) {
        this.explosionAttack = explosionAttack;
        return this;
    }
}
