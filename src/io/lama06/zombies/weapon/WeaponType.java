package io.lama06.zombies.weapon;

import io.lama06.zombies.menu.MenuDisplayableEnum;
import io.lama06.zombies.weapon.ammo.AmmoData;
import io.lama06.zombies.weapon.shoot.ShootData;
import io.lama06.zombies.weapon.shoot_particle.ShootParticleData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;

public enum WeaponType implements MenuDisplayableEnum {
    PISTOL(
            WeaponData.builder()
                    .setDisplayName(Component.text("Pistol"))
                    .setMaterial(Material.WOODEN_HOE)
                    .setAmmo(new AmmoData(100, 10))
                    .setReload(50)
                    .setDelay(30)
                    .setShoot(new ShootData(1, 5, 5, 1))
                    .setShootParticle(new ShootParticleData(Particle.FLAME))
    );

    public final WeaponData data;

    WeaponType(final WeaponData.Builder data) {
        this.data = data.build();
    }

    @Override
    public Component getDisplayName() {
        return data.displayName();
    }

    @Override
    public Material getMaterial() {
        return data.material();
    }
}
