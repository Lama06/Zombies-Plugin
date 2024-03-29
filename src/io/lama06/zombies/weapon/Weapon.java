package io.lama06.zombies.weapon;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.weapon.ammo.AmmoComponent;
import io.lama06.zombies.weapon.delay.DelayComponent;
import io.lama06.zombies.weapon.melee.MeleeComponent;
import io.lama06.zombies.weapon.reload.ReloadComponent;
import io.lama06.zombies.weapon.shoot.ShootComponent;
import io.lama06.zombies.weapon.shoot_particle.ShootParticleComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Listener;

public final class Weapon implements Listener {
    private final ZombiesPlayer player;

    private final Component displayName;
    private final Material material;
    private final AmmoComponent ammo;
    private final ReloadComponent reload;
    private final DelayComponent delay;
    private final MeleeComponent melee;
    private final ShootComponent shoot;
    private final ShootParticleComponent shootParticle;

    public Weapon(final ZombiesPlayer player, final WeaponData data) {
        this.player = player;
        displayName = data.displayName();
        material = data.material();
        ammo = data.ammo() != null ? new AmmoComponent(this, data.ammo()) : null;
        reload = data.reload() != null ? new ReloadComponent(this, data.reload()) : null;
        delay = data.delay() != null ? new DelayComponent(this, data.delay()) : null;
        melee = data.melee() != null ? new MeleeComponent(this, data.melee()) : null;
        shoot = data.shoot() != null ? new ShootComponent(this, data.shoot()) : null;
        shootParticle = data.shootParticle() != null ? new ShootParticleComponent(data.shootParticle()) : null;
    }

    public ZombiesPlayer getPlayer() {
        return player;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public AmmoComponent getAmmo() {
        return ammo;
    }

    public ReloadComponent getReload() {
        return reload;
    }

    public DelayComponent getDelay() {
        return delay;
    }

    public MeleeComponent getMelee() {
        return melee;
    }

    public ShootComponent getShoot() {
        return shoot;
    }

    public ShootParticleComponent getShootParticle() {
        return shootParticle;
    }
}
