package fr.elitgaimix.models.spigot.mongo.codecs;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.UUID;

public class UuidCodec implements Codec<UUID> {

    @Override
    public void encode(BsonWriter writer, UUID value, EncoderContext encoderContext) {
        if (value == null) {
            writer.writeNull();
        } else {
            writer.writeString(value.toString());
        }
    }

    @Override
    public UUID decode(BsonReader reader, DecoderContext decoderContext) {
        if (reader.getCurrentBsonType() == org.bson.BsonType.NULL) {
            reader.readNull();
            return null;
        } else {
            String hexString = reader.readString();
            return UUID.fromString(hexString);
        }
    }

    @Override
    public Class<UUID> getEncoderClass() {
        return UUID.class;
    }
}
