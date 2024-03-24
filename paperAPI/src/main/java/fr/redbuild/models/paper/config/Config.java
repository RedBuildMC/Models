package fr.redbuild.models.paper.config;

import java.util.UUID;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import fr.redbuild.models.paper.utils.mongo.Id;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Config {
    @BsonId
    @Id
    private UUID id;
    @BsonProperty
    private String name;
    @BsonProperty
    private Document value;

    public Config(String name) {
        this.name = name;
        this.value = new Document();
        this.id = UUID.randomUUID();
    }
    //do not use it
    public Config(){
        this.id = UUID.randomUUID();
    }

    public Config(String name, Document value) {
        this.name = name;
        this.value = value;
        this.id = UUID.randomUUID();
    }
}
