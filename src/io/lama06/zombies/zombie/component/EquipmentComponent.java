package io.lama06.zombies.zombie.component;

import io.lama06.zombies.zombie.Zombie;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Map;

public final class EquipmentComponent extends ZombieComponent {
    private final @UnmodifiableView Map<EquipmentSlot, ItemStack> equipment;

    public EquipmentComponent(final Zombie zombie, final Map<EquipmentSlot, ItemStack> equipment) {
        super(zombie);
        this.equipment = Map.copyOf(equipment);
    }

    public @UnmodifiableView Map<EquipmentSlot, ItemStack> getEquipment() {
        return equipment;
    }
}
