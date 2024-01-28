package fr.redbuild.models.spigot.grade;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import lombok.Getter;
@Getter
public class Grade {
    @BsonId
    private UUID uuid;
    @BsonProperty
    private String name;
    @BsonProperty
    private String displayName;
    @BsonProperty
    private String prefix;
    @BsonProperty
    private Map<String,Boolean> permissions;
    @BsonProperty
    private Map<String,String> attributes;

    public void addPermission(String key, Boolean value) {
        permissions.put(key, value);
    }

    public void hasPermission(String key) {
        permissions.containsKey(key);
    }
    @BsonIgnore
    public Boolean getPermission(String key) {
        return permissions.get(key);
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public void hasAttribute(String key) {
        attributes.containsKey(key);
    }
    @BsonIgnore
    public String getAttribute(String key) {
        return attributes.get(key);
    }
    
    public Grade(UUID uuid, String name, String displayName, String prefix) {
        this.uuid = uuid;
        this.name = name;
        this.displayName = displayName;
        this.prefix = prefix;
        permissions = new HashMap<>();
        attributes = new HashMap<>();
    }
}
