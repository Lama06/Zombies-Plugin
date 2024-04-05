package io.lama06.zombies;

import io.lama06.zombies.menu.*;
import io.lama06.zombies.util.BlockArea;
import io.lama06.zombies.util.EntityPosition;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class Window implements CheckableConfig {
    public String area = "";
    public EntityPosition spawnLocation;
    public BlockArea blocks;
    public BlockArea repairArea;

    public void open(final ZombiesWorld world) {
        blocks.fill(world.getBukkit(), Bukkit.createBlockData(Material.AIR));
    }

    public void close(final ZombiesWorld world) {
        blocks.fill(world.getBukkit(), Bukkit.createBlockData(Material.OAK_SLAB));
    }

    @Override
    public void check() throws InvalidConfigException {
        InvalidConfigException.mustBeSet(area, "area");
        InvalidConfigException.mustBeSet(spawnLocation, "spawn location");
        InvalidConfigException.mustBeSet(blocks, "blocks");
        InvalidConfigException.mustBeSet(repairArea, "repair area");
    }

    public void openMenu(final Player player, final Runnable callback) {
        final Runnable reopen = () -> openMenu(player, callback);

        SelectionMenu.open(
                player,
                Component.text("Window"),
                callback,
                new SelectionEntry(
                        Component.text("Area: " + (area.isEmpty() ? "_" : area)),
                        Material.OAK_FENCE,
                        () -> InputMenu.open(
                                player,
                                Component.text("Window Area"),
                                area,
                                new TextInputType(),
                                area -> {
                                    this.area = area;
                                    reopen.run();
                                },
                                reopen
                        )
                ),
                new SelectionEntry(
                        Component.text("Spawn Location: " + spawnLocation),
                        Material.OAK_PRESSURE_PLATE,
                        () -> EntityPositionSelection.open(
                                player,
                                Component.text("Window Spawn Location"),
                                reopen,
                                spawnLocation -> {
                                    this.spawnLocation = spawnLocation;
                                    reopen.run();
                                }
                        )
                ),
                new SelectionEntry(
                        Component.text("Blocks: " + blocks),
                        Material.GLASS,
                        () -> BlockAreaSelection.open(
                                player,
                                Component.text("Window Blocks"),
                                reopen,
                                blocks -> {
                                    this.blocks = blocks;
                                    reopen.run();
                                }
                        )
                ),
                new SelectionEntry(
                        Component.text("Repair Area: " + repairArea),
                        Material.BROWN_CARPET,
                        () -> BlockAreaSelection.open(
                                player,
                                Component.text("Repair Area"),
                                reopen,
                                repairArea -> {
                                    this.repairArea = repairArea;
                                    reopen.run();
                                }
                        )
                )
        );
    }
}
