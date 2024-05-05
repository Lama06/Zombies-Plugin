package io.lama06.zombies.perk;

import io.lama06.zombies.data.AttributeId;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.persistence.PersistentDataType;

public enum GlobalPerk {
    INSTANT_KILL(
            10*20,
            Material.SKELETON_SKULL,
            Component.text("Insta Kill").color(NamedTextColor.RED),
            BarColor.RED
    ),
    MAX_AMMO(
            0,
            Material.WOODEN_HOE,
            Component.text("Max Ammo").color(NamedTextColor.BLUE),
            BarColor.BLUE
    ),
    DOUBLE_GOLD(
            30*20,
            Material.GOLD_NUGGET,
            Component.text("Double Gold").color(NamedTextColor.GOLD),
            BarColor.YELLOW
    );

    private final int time;
    private final Material material;
    private final Component displayName;
    private final BarColor barColor;

    GlobalPerk(final int time, final Material material, final Component displayName, final BarColor barColor) {
        this.time = time;
        this.material = material;
        this.displayName = displayName;
        this.barColor = barColor;
    }

    public AttributeId<Integer> getRemainingTimeAttribute() {
        return new AttributeId<>(name().toLowerCase(), PersistentDataType.INTEGER);
    }

    public int getTime() {
        return time;
    }

    public Material getMaterial() {
        return material;
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public Component getDisplayName() {
        return displayName;
    }
}
