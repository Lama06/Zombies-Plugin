package io.lama06.zombies.util.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;

import java.io.IOException;

public final class BlockPositionTypeAdapter extends TypeAdapter<BlockPosition> {
    @Override
    public void write(final JsonWriter out, final BlockPosition value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("x");
        out.value(value.blockX());
        out.name("y");
        out.value(value.blockY());
        out.name("z");
        out.value(value.blockZ());
        out.endObject();
    }

    @Override
    public BlockPosition read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        in.beginObject();
        int x = 0, y = 0, z = 0;
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "x" -> x = in.nextInt();
                case "y" -> y = in.nextInt();
                case "z" -> z = in.nextInt();
            }
        }
        in.endObject();
        return Position.block(x, y, z);
    }
}
