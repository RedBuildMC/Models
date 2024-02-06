package fr.redbuild.models.spigot.npc;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;
@Getter
@Setter
public class Skin {
    @BsonProperty
    private String textureValue;
    @BsonProperty
    private String textureSignature;

    public Skin(String textureValue, String textureSignature) {
        this.textureValue = textureValue;
        this.textureSignature = textureSignature;
    }

    public Skin() {
    }
}
