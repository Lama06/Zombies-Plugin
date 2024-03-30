package io.lama06.zombies;

import io.lama06.zombies.system.*;
import io.lama06.zombies.weapon.ammo.AmmoSystem;
import io.lama06.zombies.weapon.attack.AttackSystem;
import io.lama06.zombies.weapon.delay.DelaySystem;
import io.lama06.zombies.weapon.melee.MeleeSystem;
import io.lama06.zombies.weapon.reload.ReloadSystem;
import io.lama06.zombies.weapon.shoot.ShootSystem;
import io.lama06.zombies.weapon.shoot_particle.ShootParticleSystem;
import io.lama06.zombies.zombie.system.ApplyAttackDamageSystem;
import io.lama06.zombies.zombie.system.EquipmentSystem;
import io.lama06.zombies.zombie.system.InitialHealthSystem;
import io.lama06.zombies.zombie.break_window.BreakWindowSystem;
import org.bukkit.event.Listener;

public final class Systems {
    public static Listener[] SYSTEMS = new Listener[] {
            new PowerSwitchSystem(),
            new DoorSystem(),
            new PreventEventsSystem(),
            new RoundsSystem(),
            new WindowSystem(),
            new ZombieSpawnSystem(),

            new AmmoSystem(),
            new AttackSystem(),
            new DelaySystem(),
            new MeleeSystem(),
            new ReloadSystem(),
            new ShootSystem(),
            new ShootParticleSystem(),

            new BreakWindowSystem(),
            new ApplyAttackDamageSystem(),
            new EquipmentSystem(),
            new InitialHealthSystem(),
    };

    private Systems() { }
}
