package io.lama06.zombies.weapon;

import io.lama06.zombies.weapon.ammo.AmmoData;
import io.lama06.zombies.weapon.attack.AttackData;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.melee.MeleeData;
import io.lama06.zombies.weapon.shoot.ShootData;
import io.lama06.zombies.weapon.shoot_particle.ShootParticleData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public record WeaponData(
        Component displayName,
        Material material,
        AmmoData ammo,
        @Nullable Integer delay,
        @Nullable Integer reload,
        ShootData shoot,
        ShootParticleData shootParticle,
        MeleeData melee,
        AttackData attack
) {
    public static Builder builder() {
        return new Builder();
    }

    public ItemStack createWeapon() {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        final WeaponCreateEvent event = new WeaponCreateEvent(this, item, meta);
        Bukkit.getPluginManager().callEvent(event);
        meta.getPersistentDataContainer().set(WeaponAttributes.IS_WEAPON.getKey(), PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    public static final class Builder {
        private Component displayName;
        private Material material;
        private AmmoData ammo;
        private @Nullable Integer delay;
        private @Nullable Integer reload;
        private ShootData shoot;
        private ShootParticleData shootParticle;
        private MeleeData melee;
        private AttackData attack;

        private Builder() { }

        public WeaponData build() {
            return new WeaponData(
                    displayName,
                    material,
                    ammo,
                    delay,
                    reload,
                    shoot,
                    shootParticle,
                    melee,
                    attack
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

        public Builder setShootParticle(final ShootParticleData shootParticle) {
            this.shootParticle = shootParticle;
            return this;
        }

        public Builder setMelee(final MeleeData melee) {
            this.melee = melee;
            return this;
        }

        public Builder setAttack(final AttackData attack) {
            this.attack = attack;
            return this;
        }
    }
}
