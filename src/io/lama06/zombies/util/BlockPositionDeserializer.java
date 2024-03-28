package io.lama06.zombies.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.papermc.paper.math.BlockPosition;

import java.lang.reflect.Type;

public final class BlockPositionDeserializer implements JsonDeserializer<BlockPosition> {
    @Override
    public BlockPosition deserialize(
            final JsonElement json, final Type typeOfT, final JsonDeserializationContext context
    ) throws JsonParseException {
        return null;
    }
}
