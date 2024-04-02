package io.lama06.zombies;

import io.lama06.zombies.system.*;
import io.lama06.zombies.system.player.DetectPlayerKillsZombieSystem;
import io.lama06.zombies.system.player.IncrementPlayerKillsSystem;
import io.lama06.zombies.system.player.RenderScoreboardSystem;
import io.lama06.zombies.system.weapon.RenderWeaponLoreSystem;
import io.lama06.zombies.system.weapon.ammo.*;
import io.lama06.zombies.system.weapon.attack.ApplyAttackDamageSystem;
import io.lama06.zombies.system.weapon.attack.GiveGoldAfterAttackSystem;
import io.lama06.zombies.system.weapon.attack.InitAttackSystem;
import io.lama06.zombies.system.weapon.delay.*;
import io.lama06.zombies.system.weapon.melee.AttackMeleeSystem;
import io.lama06.zombies.system.weapon.melee.InitMeleeSystem;
import io.lama06.zombies.system.weapon.melee.RenderMeleeLoreSystem;
import io.lama06.zombies.system.weapon.reload.*;
import io.lama06.zombies.system.weapon.shoot.*;
import io.lama06.zombies.system.zombie.*;
import io.lama06.zombies.system.zombie.break_window.*;
import io.lama06.zombies.system.zombie.explosion_attack.ExplodeOnDeathSystem;
import io.lama06.zombies.system.zombie.explosion_attack.ExplodePeriodicallySystem;
import io.lama06.zombies.system.zombie.explosion_attack.InitExplosionAttackSystem;
import io.lama06.zombies.system.zombie.fire_attack.InitFireAttackSystem;
import io.lama06.zombies.system.zombie.fire_attack.PerformFireAttackSystem;
import io.lama06.zombies.system.zombie.laser_attack.InitLaserAttackSystem;
import io.lama06.zombies.system.zombie.laser_attack.LaserAttackDamageSystem;
import io.lama06.zombies.system.zombie.laser_attack.MoveLaserAttackGuardianSystem;
import io.lama06.zombies.system.zombie.laser_attack.RemoveLaserAttackGuardianSystem;
import org.bukkit.event.Listener;

public final class Systems {
    public static Listener[] SYSTEMS = new Listener[] {
            new CleanupAfterGameSystem(),
            new EnablePowerSwitchSystem(),
            new InteractWithWeaponShopSystem(),
            new OpenDoorSystem(),
            new PrepareWorldAtGameStartSystem(),
            new PreventEventsSystem(),
            new RepairWindowSystem(),
            new StartNextRoundSystem(),

            // Player
            new DetectPlayerKillsZombieSystem(),
            new IncrementPlayerKillsSystem(),
            new RenderScoreboardSystem(),

            // Weapon
            new RenderWeaponLoreSystem(),

            // Ammo
            new DecrementAmmoAfterWeaponUseSystem(),
            new InitAmmoSystem(),
            new PreventWeaponUseWithEmptyClipSystem(),
            new ReloadAmmoSystem(),
            new RenderAmmoClipSystem(),
            new RenderAmmoLoreSystem(),
            new RenderTotalAmmoSystem(),

            // Attack
            new ApplyAttackDamageSystem(),
            new GiveGoldAfterAttackSystem(),
            new InitAttackSystem(),
            new RenderAmmoLoreSystem(),

            // Delay
            new InitDelaySystem(),
            new PreventWeaponUseDuringDelaySystem(),
            new RenderDelayLoreSystem(),
            new RenderDelaySystem(),
            new StartDelayAfterWeaponUseSystem(),
            new TickDelaySystem(),

            // Melee
            new AttackMeleeSystem(),
            new InitMeleeSystem(),
            new RenderMeleeLoreSystem(),

            // Reload
            new InitReloadSystem(),
            new PreventWeaponUseDuringReloadSystem(),
            new RenderReloadLoreSystem(),
            new RenderReloadSystem(),
            new StartReloadAutoSystem(),
            new StartReloadManualSystem(),
            new TickReloadSystem(),

            // Shoot
            new FireBulletsSystem(),
            new InitShootParticleSystem(),
            new InitShootSystem(),
            new RenderShootLoreSystem(),
            new SpawnParticlesAfterShotSystem(),

            // Zombie
            new DamageZombieAfterAttackSystem(),
            new InitZombieEquipmentSystem(),
            new InitZombieHealthSystem(),
            new PreventFireWhenImmuneSystem(),
            new SpawnFireTrailSystem(),
            new SpawnZombiesSystem(),

            // Break Window
            new CancelMovementDuringWindowBreakingSystem(),
            new CancelWindowBreakingOnBlockDisappearanceSystem(),
            new InitWindowBreakingSystem(),
            new PlaySoundDuringWindowBreakingSystem(),
            new StartWindowBreakingSystem(),
            new TickWindowBreakingSystem(),

            // Explosion Attack
            new ExplodeOnDeathSystem(),
            new ExplodePeriodicallySystem(),
            new InitExplosionAttackSystem(),

            // Fire Attack
            new InitFireAttackSystem(),
            new PerformFireAttackSystem(),

            // Laser Attack
            new InitLaserAttackSystem(),
            new LaserAttackDamageSystem(),
            new MoveLaserAttackGuardianSystem(),
            new RemoveLaserAttackGuardianSystem()
    };

    private Systems() { }
}
