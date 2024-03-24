package fr.redbuild.models.paper.world;

import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import fr.redbuild.models.paper.utils.mongo.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SWorld {
    @BsonId
    @Id
    private UUID uuid = UUID.randomUUID();
    @BsonProperty
    private String name;
    @BsonProperty
    private String displayname;
    @BsonProperty
    private String description;
    @BsonProperty
    private String permission;

    public SWorld() {
    }

    public SWorld(UUID uuid, String name, String displayname, String description, String permission) {
        this.uuid = uuid;
        this.name = name;
        this.displayname = displayname;
        this.description = description;
        this.permission = permission;
    }
    
    public SWorld(String name, String displayname, String description, String permission) {
        this.name = name;
        this.displayname = displayname;
        this.description = description;
        this.permission = permission;
    }
}
