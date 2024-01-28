package fr.redbuild.models.spigot.mongo.codecs;

import java.util.UUID;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerCodec implements Codec<Player>{

    @Override
    public void encode(BsonWriter writer, Player value, EncoderContext encoderContext) {
        writer.writeString(value.getUniqueId().toString());
    }

    @Override
    public Class<Player> getEncoderClass() {
        return Player.class;
    }

    @Override
    public Player decode(BsonReader reader, DecoderContext decoderContext) {
        return Bukkit.getServer().getPlayer(UUID.fromString(reader.readString()));
    }
    
}
