package fr.redbuild.models.spigot.mongo.codecs;

import fr.redbuild.models.spigot.npc.NPC;
import fr.redbuild.models.spigot.npc.Skin;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class NPCCodec implements Codec<NPC> {

    @Override
    public NPC decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        UUID npcUUID = UUID.fromString(reader.readString("_id"));
        String name = reader.readString("name");
        Skin skin = new Skin(reader.readString("textureValue"), reader.readString("textureSignature"));
        int entityID = reader.readInt32("entityID");
        Location npcLocation = new Location(Bukkit.getWorld(reader.readString("world")), reader.readDouble("x"), reader.readDouble("y"), reader.readDouble("z"), (float) reader.readDouble("yaw"), (float) reader.readDouble("pitch"));
        boolean visibleName = reader.readBoolean("visibleName");
        reader.readEndDocument();
        return new NPC(npcUUID, name, skin, entityID, npcLocation).setVisibleName(visibleName);
    }

    @Override
    public void encode(BsonWriter writer, NPC npc, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("_id", npc.npcUUID.toString());
        writer.writeString("name", npc.name);
        writer.writeString("textureValue", npc.skin.getTextureValue());
        writer.writeString("textureSignature", npc.skin.getTextureSignature());
        writer.writeInt32("entityID", npc.entityID);
        writer.writeString("world", npc.npcLocation.getWorld().getName());
        writer.writeDouble("x", npc.npcLocation.getX());
        writer.writeDouble("y", npc.npcLocation.getY());
        writer.writeDouble("z", npc.npcLocation.getZ());
        writer.writeDouble("yaw", npc.npcLocation.getYaw());
        writer.writeDouble("pitch", npc.npcLocation.getPitch());
        writer.writeBoolean("visibleName", npc.visibleName);
        writer.writeEndDocument();
    }

    @Override
    public Class<NPC> getEncoderClass() {
        return NPC.class;
    }
}