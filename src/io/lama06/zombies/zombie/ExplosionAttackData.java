package io.lama06.zombies.zombie;

public record ExplosionAttackData(Integer delay, boolean onDeath, double damage) {
    public static ExplosionAttackData explodeOnDeath(final double damage) {
        return new ExplosionAttackData(0, true, damage);
    }

    public static ExplosionAttackData explodePeriodically(final int delay, final double damage) {
        return new ExplosionAttackData(delay, false, damage);
    }
}
