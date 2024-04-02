package io.lama06.zombies.system.weapon.shoot;

import io.lama06.zombies.data.Component;
import io.lama06.zombies.weapon.Weapon;
import io.lama06.zombies.weapon.WeaponComponents;
import io.lama06.zombies.event.weapon.WeaponCreateEvent;
import io.lama06.zombies.weapon.ShootParticleData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class InitShootParticleSystem implements Listener {
    @EventHandler
    private void onWeaponCreate(final WeaponCreateEvent event) {
        final ShootParticleData data = event.getData().shootParticle;
        if (data == null) {
            return;
        }
        final Weapon weapon = event.getWeapon();
        final Component shootParticleComponent = weapon.addComponent(WeaponComponents.SHOOT_PARTICLE);
        shootParticleComponent.set(ShootParticleData.PARTICLE, data.particle());
        shootParticleComponent.set(ShootParticleData.COUNT, data.count());
        shootParticleComponent.set(ShootParticleData.SPACING, data.spacing());
    }
}
