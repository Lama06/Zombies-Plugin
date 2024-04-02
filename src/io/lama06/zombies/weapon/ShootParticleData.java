package io.lama06.zombies.weapon;

import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.util.pdc.RegistryPersistentDataType;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.persistence.PersistentDataType;

public record ShootParticleData(
    Particle particle,
    int count,
    double spacing
) {
    public static final AttributeId<Particle> PARTICLE = new AttributeId<>(
            "particle",
            new RegistryPersistentDataType<>(Particle.class, Registry.PARTICLE_TYPE)
    );
    public static final AttributeId<Integer> COUNT = new AttributeId<>("count", PersistentDataType.INTEGER);
    public static final AttributeId<Double> SPACING = new AttributeId<>("spacing", PersistentDataType.DOUBLE);

    public ShootParticleData(final Particle particle) {
        this(particle, 15, 1);
    }
}
