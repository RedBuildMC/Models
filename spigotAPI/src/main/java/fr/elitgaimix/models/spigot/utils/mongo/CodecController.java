package fr.elitgaimix.models.spigot.utils.mongo;

import com.mongodb.MongoClientSettings;

import fr.elitgaimix.models.spigot.grade.Grade;
import fr.elitgaimix.models.spigot.mongo.codecs.*;
import fr.elitgaimix.models.spigot.npc.NPC;
import fr.elitgaimix.models.spigot.npc.Skin;
import fr.elitgaimix.models.spigot.region.Region;
import fr.elitgaimix.models.spigot.user.User;
import fr.elitgaimix.models.spigot.world.SWorld;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;

public class CodecController {
    private final List<Codec<?>> codecs = new ArrayList<>();
    private final List<CodecProvider> codecProviders = new ArrayList<>();

    public CodecController() {
    }
    public void initialize(){
        addAll(List.of(new ItemStackCodec(),new LocationCodec(),new WorldCodec(),new UuidCodec()));
        this.codecProviders.add(PojoCodecProvider.builder().register(
                //Region
                Region.class
        ).build());
    }

    @Deprecated
    public void registerCodec(Codec<?> codec) {
        this.codecs.add(codec);
    }

    public void add(Codec<?> codec) {
        this.codecs.add(codec);
    }

    public void addAll(List<Codec<?>> codec) {
        this.codecs.addAll(codec);
    }

    public void registerCodecProvider(CodecProvider provider) {
        this.codecProviders.add(provider);
    }

    /**
     * @return
     */
    public CodecRegistry getCodecRegistries() {
        return CodecRegistries.fromRegistries(CodecRegistries.fromCodecs(this.codecs),CodecRegistries.fromCodecs(new ItemStackCodec(),new LocationCodec(),new WorldCodec(),new UuidCodec(),new PlayerCodec())
                ,CodecRegistries.fromProviders(PojoCodecProvider.builder().register(
                        //Region
                        Region.class,
                        //World
                        SWorld.class,
                        //User
                        User.class,Grade.class,
                        //NPC
                        NPC.class,Skin.class
                ).build()),CodecRegistries.fromProviders(this.codecProviders),
                MongoClientSettings.getDefaultCodecRegistry());
    }

}
 