package io.lama06.zombies.perk;

import io.lama06.zombies.ZombiesPlayer;
import io.lama06.zombies.data.AttributeId;
import io.lama06.zombies.menu.MenuDisplayableEnum;
import io.lama06.zombies.util.pdc.EnumPersistentDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum PlayerPerk implements MenuDisplayableEnum {
    FLAME_BULLETS(Material.FIRE_CHARGE, Component.text("Flame Bullets").color(NamedTextColor.YELLOW)),
    FROZEN_BULLETS(Material.ICE, Component.text("Frozen Bullets").color(NamedTextColor.BLUE)),
    SPEED(Material.RABBIT_FOOT, Component.text("Speed").color(NamedTextColor.YELLOW)) {
        @Override
        public void enable(final ZombiesPlayer player) {
            player.getBukkit().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1));
        }

        @Override
        public void disable(final ZombiesPlayer player) {
            player.getBukkit().removePotionEffect(PotionEffectType.SPEED);
        }
    },
    QUICK_FIRE(Material.WOODEN_HOE, Component.text("Quick Fire").color(NamedTextColor.LIGHT_PURPLE)),
    EXTRA_HEALTH(Material.GOLDEN_APPLE, Component.text("Extra Health").color(NamedTextColor.RED)) {
        @Override
        public void enable(final ZombiesPlayer player) {
            final AttributeInstance maxHealth = player.getBukkit().getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealth == null) {
                return;
            }
            maxHealth.setBaseValue(30);
            player.getBukkit().setHealth(30);
        }

        @Override
        public void disable(final ZombiesPlayer player) {
            final AttributeInstance maxHealth = player.getBukkit().getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealth == null) {
                return;
            }
            maxHealth.setBaseValue(20);
        }
    },
    EXTRA_WEAPON(Material.CHEST, Component.text("Extra Weapon").color(NamedTextColor.GREEN));

    public static final AttributeId<Boolean> IS_PLAYER_PERK = new AttributeId<>("is_player_perk", PersistentDataType.BOOLEAN);
    public static final AttributeId<PlayerPerk> TYPE = new AttributeId<>("type", new EnumPersistentDataType<>(PlayerPerk.class));

    private final Material material;
    private final Component name;

    PlayerPerk(final Material material, final Component name) {
        this.material = material;
        this.name = name;
    }

    public void enable(final ZombiesPlayer player) { }

    public void disable(final ZombiesPlayer player) { }

    @Override
    public Material getDisplayMaterial() {
        return material;
    }

    @Override
    public Component getDisplayName() {
        return name;
    }
}
