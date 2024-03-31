package io.lama06.zombies.zombie;

import io.lama06.zombies.zombie.break_window.BreakWindowData;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public record ZombieData(
        EntityType entity,
        int health,
        Map<EquipmentSlot, ItemStack> equipment,
        BreakWindowData breakWindow
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private EntityType entity;
        private int health;
        private final Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();
        private BreakWindowData breakWindow;

        private Builder() { }

        public ZombieData build() {
            return new ZombieData(
                    entity,
                    health,
                    equipment,
                    breakWindow
            );
        }

        public Builder setEntity(final EntityType entity) {
            this.entity = entity;
            return this;
        }

        public Builder setHealth(final int health) {
            this.health = health;
            return this;
        }

        public Builder addEquipment(final EquipmentSlot slot, final ItemStack item) {
            equipment.put(slot, item);
            return this;
        }

        public Builder setBreakWindow(final BreakWindowData breakWindow) {
            this.breakWindow = breakWindow;
            return this;
        }
    }
}
