package io.lama06.zombies.weapon;

import io.lama06.zombies.weapon.component.AmmoData;
import io.lama06.zombies.weapon.component.MeleeData;
import io.lama06.zombies.weapon.component.ShootData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public record WeaponData(
        Component displayName,
        Material material,
        AmmoData ammo,
        @Nullable Integer delay,
        @Nullable Integer reload,
        ShootData shoot,
        MeleeData melee
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Component displayName;
        private Material material;
        private AmmoData ammo;
        private @Nullable Integer delay;
        private @Nullable Integer reload;
        private ShootData shoot;
        private MeleeData melee;

        private Builder() { }

        public WeaponData build() {
            return new WeaponData(
                    displayName,
                    material,
                    ammo,
                    delay,
                    reload,
                    shoot,
                    melee
            );
        }

        public Builder setDisplayName(final Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setMaterial(final Material material) {
            this.material = material;
            return this;
        }

        public Builder setAmmo(final AmmoData ammo) {
            this.ammo = ammo;
            return this;
        }

        public Builder setDelay(final @Nullable Integer delay) {
            this.delay = delay;
            return this;
        }

        public Builder setReload(final @Nullable Integer reload) {
            this.reload = reload;
            return this;
        }

        public Builder setShoot(final ShootData shoot) {
            this.shoot = shoot;
            return this;
        }

        public Builder setMelee(final MeleeData melee) {
            this.melee = melee;
            return this;
        }
    }
}
