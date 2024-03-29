package io.lama06.zombies.weapon.shoot_particle;

import org.bukkit.Particle;

public final class ShootParticleComponent {
    private final Particle particle;
    private final int numberOfParticles;
    private final double distanceBetweenParticles;

    public ShootParticleComponent(final ShootParticleData data) {
        particle = data.particle();
        numberOfParticles = data.numberOfParticles();
        distanceBetweenParticles = data.distanceBetweenParticles();
    }

    public Particle getParticle() {
        return particle;
    }

    public int getNumberOfParticles() {
        return numberOfParticles;
    }

    public double getDistanceBetweenParticles() {
        return distanceBetweenParticles;
    }
}
