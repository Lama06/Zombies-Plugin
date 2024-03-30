package io.lama06.zombies.weapon.shoot_particle;

import org.bukkit.Particle;

public record ShootParticleData(
    Particle particle,
    int numberOfParticles,
    double spacing
) {
    public ShootParticleData(final Particle particle) {
        this(particle, 15, 1);
    }
}
