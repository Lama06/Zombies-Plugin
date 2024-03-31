package io.lama06.zombies.weapon;

import org.bukkit.Particle;

public record ShootParticleData(
    Particle particle,
    int count,
    double spacing
) {
    public ShootParticleData(final Particle particle) {
        this(particle, 15, 1);
    }
}
