package fr.redbuild.models.spigot.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.redbuild.models.spigot.grade.Grade;
import fr.redbuild.models.spigot.grade.GradeManager;
import fr.redbuild.models.spigot.utils.injector.Injector;
import fr.redbuild.models.spigot.utils.mongo.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    @BsonId
    @Id
    private UUID uuid;
    @BsonProperty
    private String displayName;
    @BsonProperty
    private UUID grade;
    @BsonProperty
    private Date firstConnection;
    @BsonProperty
    private Map<String,String> attributes;
    @BsonIgnore
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }
    @BsonIgnore
    public void removeAttribute(String key) {
        attributes.remove(key);
    }
    @BsonIgnore
    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }
    @BsonIgnore
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }
    @BsonIgnore
    public String getAttribute(String key) {
        return attributes.get(key);
    }
    @BsonIgnore
    public Player getPlayer(){
        return Bukkit.getServer().getPlayer(uuid);
    }

    @BsonIgnore
    public Grade getUserGrade(){
        return Injector.getInstance(GradeManager.class).getGrade(grade);
    }
    @BsonIgnore
    public void addGrade(Grade grade){
        this.grade = grade.getUuid();
    }

    public User(UUID uuid, String name, UUID grade,Date firstConnection) {
        this.uuid = uuid;
        this.displayName = name;
        this.grade = grade;
        this.firstConnection = firstConnection;
        attributes = new HashMap<>();
    }

    public User(){
    }
    @BsonIgnore
    public String toString(){
        return "User(uuid="+uuid+",displayName="+displayName+",grade="+grade+",firstConnection="+firstConnection+",attribute="+attributes+")";
    }

    
}
