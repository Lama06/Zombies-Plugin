package io.lama06.zombies.weapon.shoot;

import io.lama06.zombies.event.PlayerAttacksZombieEvent;
import io.lama06.zombies.util.VectorUtil;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponAttributes;
import io.lama06.zombies.weapon.event.WeaponCreateEvent;
import io.lama06.zombies.weapon.render.LoreEntry;
import io.lama06.zombies.weapon.render.LorePart;
import io.lama06.zombies.weapon.render.WeaponLoreRenderEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public final class ShootSystem implements Listener {
    @EventHandler
    private void createWeapon(final WeaponCreateEvent event) {
        final ShootData data = event.getData().shoot();
        if (data == null) {
            return;
        }
        final PersistentDataContainer pdc = event.getPdc();
        final PersistentDataContainer shootContainer = pdc.getAdapterContext().newPersistentDataContainer();
        shootContainer.set(ShootAttributes.BULLETS.getKey(), PersistentDataType.INTEGER, data.bullets());
        shootContainer.set(ShootAttributes.PRECISION.getKey(), PersistentDataType.DOUBLE, data.precision());
        pdc.set(WeaponAttributes.SHOOT.getKey(), PersistentDataType.TAG_CONTAINER, shootContainer);
    }

    @EventHandler
    private void renderLore(final WeaponLoreRenderEvent event) {
        final PersistentDataContainer pdc = event.getWeapon().getItem().getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer container = pdc.get(WeaponAttributes.SHOOT.getKey(), PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final Integer bullets = pdc.get(ShootAttributes.BULLETS.getKey(), PersistentDataType.INTEGER);
        final Double precision = pdc.get(ShootAttributes.PRECISION.getKey(), PersistentDataType.DOUBLE);
        if (bullets == null || precision == null) {
            return;
        }
        event.addLore(LorePart.SHOOT, List.of(
                new LoreEntry("Bullets", bullets.toString()),
                new LoreEntry("Precision", Math.round(precision * 100) + "%")
        ));
    }

    @EventHandler
    private void onPlayerAttackEntity(final PrePlayerAttackEntityEvent event) {
        onLeftClick(event.getPlayer());
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
            return;
        }
        onLeftClick(event.getPlayer());
    }

    private void onLeftClick(final Player player) {
        final Weapon weapon = Weapon.getHeldWeapon(player);
        if (weapon == null) {
            return;
        }
        final ItemStack item = weapon.getItem();
        if (item == null) {
            return;
        }
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        final PersistentDataContainer shootContainer = pdc.get(WeaponAttributes.SHOOT.getKey(), PersistentDataType.TAG_CONTAINER);
        if (shootContainer == null) {
            return;
        }
        final Integer bulletCount = shootContainer.get(ShootAttributes.BULLETS.getKey(), PersistentDataType.INTEGER);
        final Double precision = shootContainer.get(ShootAttributes.PRECISION.getKey(), PersistentDataType.DOUBLE);
        if (bulletCount == null || precision == null) {
            return;
        }
        final RandomGenerator rnd = ThreadLocalRandom.current();
        final List<Bullet> bullets = new ArrayList<>();
        for (int i = 0; i < bulletCount; i++) {
            final float yaw = (float) (player.getYaw() +
                    (1 - precision) * rnd.nextDouble() * 10 * (rnd.nextBoolean() ? 1 : -1));
            final float pitch = (float) (player.getPitch() +
                    (1 - precision) * rnd.nextDouble() * 5 * (rnd.nextBoolean() ? 1 : -1));
            final Vector bulletDirection = VectorUtil.fromJawAndPitch(yaw, pitch);
            bullets.add(new Bullet(bulletDirection));
        }
        if (!new WeaponShootEvent(weapon, bullets).callEvent()) {
            return;
        }
        for (final Bullet bullet : bullets) {
            detectShotAtZombie(player, weapon, bullet);
        }
    }

    private void detectShotAtZombie(final Player player, final Weapon weapon, final Bullet bullet) {
        final RayTraceResult ray = player.getWorld().rayTraceEntities(
                player.getEyeLocation(),
                bullet.direction(),
                50,
                entity -> !entity.equals(player)
        );
        if (ray == null) {
            return;
        }
        final Entity zombie = ray.getHitEntity();
        if (zombie == null) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerAttacksZombieEvent(weapon, zombie));
    }
}
