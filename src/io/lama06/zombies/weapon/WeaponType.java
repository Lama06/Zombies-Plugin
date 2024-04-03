package io.lama06.zombies.weapon;

import io.lama06.zombies.menu.MenuDisplayableEnum;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;

public enum WeaponType implements MenuDisplayableEnum {
    KNIFE(
            new WeaponData()
                    .setDisplayName(Component.text("Knife"))
                    .setMaterial(Material.IRON_SWORD)
                    .setDelay(new DelayData(30))
                    .setMelee(new MeleeData(5))
                    .setAttack(new AttackData(5, false, 10))
    ),
    PISTOL(
            new WeaponData()
                    .setDisplayName(Component.text("Pistol"))
                    .setMaterial(Material.WOODEN_HOE)
                    .setAmmo(new AmmoData(100, 10))
                    .setReload(new ReloadData(50))
                    .setDelay(new DelayData(30))
                    .setShoot(new ShootData(1, 1))
                    .setShootParticle(new ShootParticleData(Particle.FLAME))
                    .setAttack(new AttackData(5, false, 10))
    );

    public final WeaponData data;

    WeaponType(final WeaponData data) {
        this.data = data;
    }

    @Override
    public Component getDisplayName() {
        return data.displayName;
    }

    @Override
    public Material getDisplayMaterial() {
        return data.material;
    }
}
