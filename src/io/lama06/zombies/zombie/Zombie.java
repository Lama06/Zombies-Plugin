package io.lama06.zombies.zombie;

import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.component.EquipmentComponent;
import io.lama06.zombies.zombie.component.HealthComponent;
import org.bukkit.entity.Entity;

public final class Zombie {
    private final ZombiesGame game;
    private final Entity entity;

    private final HealthComponent health;
    private final EquipmentComponent equipment;

    public Zombie(final ZombiesGame game, final Entity entity, final ZombieData data) {
        this.game = game;
        this.entity = entity;
        health = new HealthComponent(this, data.health());
        equipment = !data.equipment().isEmpty() ? new EquipmentComponent(this, data.equipment()) : null;
    }

    public ZombiesGame getGame() {
        return game;
    }

    public Entity getEntity() {
        return entity;
    }

    public HealthComponent getHealth() {
        return health;
    }

    public EquipmentComponent getEquipment() {
        return equipment;
    }
}
