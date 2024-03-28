package io.lama06.zombies.weapon;

import io.lama06.zombies.menu.MenuDisplayableEnum;
import io.lama06.zombies.weapon.component.AmmoData;
import io.lama06.zombies.weapon.component.ShootData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public enum WeaponType implements MenuDisplayableEnum {
    PISTOL(
            WeaponData.builder()
                    .setDisplayName(Component.text("Pistol"))
                    .setMaterial(Material.WOODEN_HOE)
                    .setAmmo(new AmmoData(100, 10))
                    .setReload(50)
                    .setDelay(30)
                    .setShoot(new ShootData(1, 5, 5, 1))
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
