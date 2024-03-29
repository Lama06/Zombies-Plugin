package io.lama06.zombies;

import io.lama06.zombies.system.EnablePowerSwitchSystem;
import io.lama06.zombies.system.OpenDoorSystem;
import io.lama06.zombies.system.PreventEventsSystem;
import io.lama06.zombies.system.ZombieSpawnSystem;
import io.lama06.zombies.weapon.ammo.*;
import io.lama06.zombies.weapon.delay.DelayRenderSystem;
import io.lama06.zombies.weapon.delay.DelayTickSystem;
import io.lama06.zombies.weapon.delay.PreventWeaponUseDuringDelaySystem;
import io.lama06.zombies.weapon.delay.StartDelayAfterWeaponUseSystem;
import io.lama06.zombies.weapon.melee.MeleeSystem;
import io.lama06.zombies.weapon.reload.*;
import io.lama06.zombies.weapon.render.WeaponsRenderSystem;
import io.lama06.zombies.weapon.shoot_particle.ShootParticleSystem;
import io.lama06.zombies.weapon.shoot.ShootSystem;
import io.lama06.zombies.zombie.break_window.system.*;
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

            // Ammo
            AmmoClipRenderSystem::new,
            AmmoReloadSystem::new,
            AmmoTotalRenderSystem::new,
            DecrementClipOnShootSystem::new,
            PreventShootWithEmptyClipSystem::new,

            // Delay
            DelayRenderSystem::new,
            DelayTickSystem::new,
            PreventWeaponUseDuringDelaySystem::new,
            StartDelayAfterWeaponUseSystem::new,

            // Melee
            MeleeSystem::new,

            // Reload
            PreventShootDuringReloadSystem::new,
            ReloadRenderSystem::new,
            ReloadTickSystem::new,
            ReloadTriggerAutoSystem::new,
            ReloadTriggerManualSystem::new,

            // Render
            WeaponsRenderSystem::new,

            // Shoot
            ShootParticleSystem::new,
            ShootSystem::new,

            // Related to Zombies

            ApplyEquipmentSystem::new,
            DetectShotAtZombieSystem::new,
            RemoveDeadZombiesSystem::new,

            // Break Window
            BreakWindowCancelSystem::new,
            BreakWindowCompleteSystem::new,
            BreakWindowFreezeSystem::new,
            BreakWindowSoundSystem::new,
            BreakWindowStartSystem::new,
            BreakWindowTickSystem::new
    );

    private Systems() { }
}
