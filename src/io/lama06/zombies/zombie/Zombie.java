package io.lama06.zombies.zombie;

import io.lama06.zombies.ZombiesGame;
import io.lama06.zombies.zombie.break_window.BreakWindowComponent;
import io.lama06.zombies.zombie.component.EquipmentComponent;
import io.lama06.zombies.zombie.component.HealthComponent;
import org.bukkit.entity.Entity;

public final class Zombie {
    private final ZombiesGame game;
    private final Entity entity;

    private final HealthComponent health;
    private final EquipmentComponent equipment;
    private final BreakWindowComponent breakWindow;

    public Zombie(final ZombiesGame game, final Entity entity, final ZombieData data) {
        this.game = game;
        this.entity = entity;
        health = new HealthComponent(this, data.health());
        equipment = !data.equipment().isEmpty() ? new EquipmentComponent(this, data.equipment()) : null;
        breakWindow = data.breakWindow() != null ? new BreakWindowComponent(this, data.breakWindow()) : null;
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

    public BreakWindowComponent getBreakWindow() {
        return breakWindow;
    }
}
