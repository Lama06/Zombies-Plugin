package io.lama06.zombies.zombie;

import io.lama06.zombies.data.AttributeId;
import org.bukkit.persistence.PersistentDataType;

public record ExplosionAttackData(Integer delay, boolean onDeath, double damage) {
    public static final AttributeId<Integer> DELAY = new AttributeId<>("delay", PersistentDataType.INTEGER);
    public static final AttributeId<Boolean> ON_DEATH = new AttributeId<>("on_death", PersistentDataType.BOOLEAN);
    public static final AttributeId<Double> DAMAGE = new AttributeId<>("damage", PersistentDataType.DOUBLE);

    public static ExplosionAttackData explodeOnDeath(final double damage) {
        return new ExplosionAttackData(0, true, damage);
    }

    public static ExplosionAttackData explodePeriodically(final int delay, final double damage) {
        return new ExplosionAttackData(delay, false, damage);
    }
}
