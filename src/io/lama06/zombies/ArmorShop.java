package io.lama06.zombies;

import io.lama06.zombies.menu.*;
import io.lama06.zombies.util.PositionUtil;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;
import java.util.Set;

public final class ArmorShop {
    public BlockPosition position;
    public int price;
    public Part part = Part.UPPER_BODY;
    public Quality quality = Quality.LEATHER;

    public void openMenu(final Player player, final Runnable callback) {
        final Runnable reopen = () -> openMenu(player, callback);
        SelectionMenu.open(
                player,
                Component.text("Armor Shop"),
                callback,
                new SelectionEntry(
                        Component.text("Position: " + PositionUtil.format(position)),
                        Material.LEVER,
                        () -> BlockPositionSelection.open(
                                player,
                                Component.text("Armor Shop Position"),
                                reopen,
                                position -> {
                                    this.position = position;
                                    reopen.run();
                                }
                        )
                ),
                new SelectionEntry(
                        Component.text("Price: " + price),
                        Material.GOLD_NUGGET,
                        () -> InputMenu.open(
                                player,
                                Component.text("Amor Shop Price"),
                                price,
                                new IntegerInputType(),
                                price -> {
                                    this.price = price;
                                    reopen.run();
                                },
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Part: ").append(part.getDisplayName()),
                        part.getDisplayMaterial(),
                        () -> EnumSelectionMenu.open(
                                Part.class,
                                player,
                                Component.text("Armor Part"),
                                reopen,
                                part -> {
                                    this.part = part;
                                    reopen.run();
                                }
                        )
                ),
                new SelectionEntry(
                        Component.text("Quality: ").append(quality.getDisplayName()),
                        quality.getDisplayMaterial(),
                        () -> EnumSelectionMenu.open(
                                Quality.class,
                                player,
                                Component.text("Armor Quality"),
                                reopen,
                                quality -> {
                                    this.quality = quality;
                                    reopen.run();
                                }
                        )
                )
        );
    }

    public enum Part implements MenuDisplayableEnum {
        UPPER_BODY,
        LOWER_BODY;

        @Override
        public Component getDisplayName() {
            return Component.text(switch (this) {
                case LOWER_BODY -> "Lower Body";
                case UPPER_BODY -> "Upper Body";
            });
        }

        @Override
        public Material getDisplayMaterial() {
            return switch (this) {
                case LOWER_BODY -> Material.CHAINMAIL_LEGGINGS;
                case UPPER_BODY -> Material.CHAINMAIL_BOOTS;
            };
        }

        public Set<EquipmentSlot> getEquipmentSlots() {
            return switch (this) {
                case UPPER_BODY -> Set.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST);
                case LOWER_BODY -> Set.of(EquipmentSlot.LEGS, EquipmentSlot.FEET);
            };
        }
    }

    public enum Quality implements MenuDisplayableEnum {
        LEATHER(Map.of(
                EquipmentSlot.HEAD, Material.LEATHER_HELMET,
                EquipmentSlot.CHEST, Material.LEATHER_CHESTPLATE,
                EquipmentSlot.LEGS, Material.LEATHER_LEGGINGS,
                EquipmentSlot.FEET, Material.LEATHER_BOOTS
        )),
        GOLD(Map.of(
                EquipmentSlot.HEAD, Material.GOLDEN_HELMET,
                EquipmentSlot.CHEST, Material.GOLDEN_CHESTPLATE,
                EquipmentSlot.LEGS, Material.GOLDEN_LEGGINGS,
                EquipmentSlot.FEET, Material.GOLDEN_BOOTS
        )),
        IRON(Map.of(
                EquipmentSlot.HEAD, Material.IRON_HELMET,
                EquipmentSlot.CHEST, Material.IRON_CHESTPLATE,
                EquipmentSlot.LEGS, Material.IRON_LEGGINGS,
                EquipmentSlot.FEET, Material.IRON_BOOTS
        ));

        public final Map<EquipmentSlot, Material> materials;

        Quality(final Map<EquipmentSlot, Material> materials) {
            this.materials = materials;
        }

        @Override
        public Component getDisplayName() {
            return switch (this) {
                case LEATHER -> Component.text("Leather");
                case GOLD -> Component.text("Gold").color(NamedTextColor.GOLD);
                case IRON -> Component.text("Iron");
            };
        }

        @Override
        public Material getDisplayMaterial() {
            return switch (this) {
                case LEATHER -> Material.LEATHER;
                case GOLD -> Material.GOLD_INGOT;
                case IRON -> Material.IRON_INGOT;
            };
        }
    }
}
