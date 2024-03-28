package io.lama06.zombies;

import io.lama06.zombies.system.EnablePowerSwitchSystem;
import io.lama06.zombies.system.OpenDoorSystem;
import io.lama06.zombies.system.PreventEventsSystem;
import io.lama06.zombies.system.ZombieSpawnSystem;
import io.lama06.zombies.weapon.system.*;
import io.lama06.zombies.zombie.system.ApplyEquipmentSystem;
import io.lama06.zombies.zombie.system.DetectShotAtZombieSystem;
import io.lama06.zombies.zombie.system.RemoveDeadZombiesSystem;

import java.util.List;
import java.util.function.Function;

public final class Systems {
    public static final List<Function<ZombiesGame, System>> SYSTEMS = List.of(
            // Global
            EnablePowerSwitchSystem::new,
            OpenDoorSystem::new,
            ZombieSpawnSystem::new,
            PreventEventsSystem::new,

            // Related to Weapons
            AmmoClipRenderSystem::new,
            AmmoReloadSystem::new,
            AmmoTotalRenderSystem::new,
            DelayRenderSystem::new,
            DelayTickSystem::new,
            ReloadRenderSystem::new,
            ReloadTickSystem::new,
            ReloadTriggerAutoSystem::new,
            ReloadTriggerManualSystem::new,
            ShootSystem::new,
            WeaponsRenderSystem::new,

            // Related to Zombies
            ApplyEquipmentSystem::new,
            DetectShotAtZombieSystem::new,
            RemoveDeadZombiesSystem::new
    );

    private Systems() { }
}
