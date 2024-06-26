package io.lama06.zombies.util;

import org.bukkit.util.Vector;

import java.util.Random;

public final class VectorUtil {
    private static final Random RANDOM = new Random();


    public static Vector fromJawAndPitch(final float yaw, final float pitch) {
        final double yawRad = Math.toRadians(yaw);
        final double pitchRad = Math.toRadians(pitch);
        // https://wiki.vg/Protocol
        return new Vector(
                -Math.sin(yawRad)*Math.cos(pitchRad),
                -Math.sin(pitchRad),
                Math.cos(pitchRad)*Math.cos(yawRad)
        );
    }
}
