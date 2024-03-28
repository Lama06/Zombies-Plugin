package io.lama06.zombies.util.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;

import java.io.IOException;

public final class FinePositionTypeAdapter extends TypeAdapter<FinePosition> {
    @Override
    public void write(final JsonWriter out, final FinePosition value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("x");
        out.value(value.x());
        out.name("y");
        out.value(value.y());
        out.name("z");
        out.value(value.z());
        out.endObject();
    }

    @Override
    public FinePosition read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        in.beginObject();
        double x = 0, y = 0, z = 0;
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "x" -> x = in.nextDouble();
                case "y" -> y = in.nextDouble();
                case "z" -> z = in.nextDouble();
            }
        }
        in.endObject();
        return Position.fine(x, y, z);
    }
}
