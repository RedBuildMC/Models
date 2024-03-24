package fr.redbuild.models.paper.block;

import org.bson.codecs.pojo.annotations.BsonId;

// import java.util.UUID;

// import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;

// import fr.redbuild.models.paper.utils.injector.Injector;
// import fr.redbuild.models.paper.utils.mongo.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
// import java.util.ArrayList;
// import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData; 


import fr.redbuild.models.paper.plugin.PluginController;
import fr.redbuild.models.paper.utils.mongo.Id;

import java.io.Serializable;
@Getter
@Setter
public class Area implements Serializable{
    // https://github.com/Otomny/flow/tree/main/spigot/src/main/java/fr/omny/flow/world
    @BsonId
    @Id
    private UUID uuid = UUID.randomUUID();
    @BsonProperty
    private Map<String,String> attributes = new HashMap<>();

    @BsonProperty
    private final Map<SerializableLocation, String> blockStates = new HashMap<>();

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

    public Area(Location location1, Location location2,Location location3) {
        int minX = Math.min(location1.getBlockX(), location2.getBlockX());
        int minY = Math.min(location1.getBlockY(), location2.getBlockY());
        int minZ = Math.min(location1.getBlockZ(), location2.getBlockZ());

        int maxX = Math.max(location1.getBlockX(), location2.getBlockX());
        int maxY = Math.max(location1.getBlockY(), location2.getBlockY());
        int maxZ = Math.max(location1.getBlockZ(), location2.getBlockZ());

        World world = location1.getWorld();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location currentLocation = new Location(world, x, y, z);
                    Block block = currentLocation.getBlock();
                    String blockdata = block.getBlockData().getAsString();
                    blockStates.put(new SerializableLocation(currentLocation), blockdata);
                }
            }
        }
        alignToZero(location3);
    }

    public Area(Location location1,Location location2){
        int minX = Math.min(location1.getBlockX(), location2.getBlockX());
        int minY = Math.min(location1.getBlockY(), location2.getBlockY());
        int minZ = Math.min(location1.getBlockZ(), location2.getBlockZ());

        int maxX = Math.max(location1.getBlockX(), location2.getBlockX());
        int maxY = Math.max(location1.getBlockY(), location2.getBlockY());
        int maxZ = Math.max(location1.getBlockZ(), location2.getBlockZ());
        
        World world = location1.getWorld();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location currentLocation = new Location(world, x, y, z);
                    Block block = currentLocation.getBlock();
                    String blockdata = block.getBlockData().getAsString();
                    blockStates.put(new SerializableLocation(currentLocation), blockdata);
                }
            }
        }
        System.out.println(blockStates.values());
        alignToZero(location1);
        System.out.println(blockStates.values());
    }

    public void align(Location location) {
        Map<SerializableLocation, String> newBlockStates = new HashMap<>();
        for (Map.Entry<SerializableLocation, String> entry : blockStates.entrySet()) {
            Location currentLocation = entry.getKey().toLocation().add(location);
            newBlockStates.put(new SerializableLocation(currentLocation), entry.getValue());
        }
        blockStates.clear();
        blockStates.putAll(newBlockStates);
    }

    public void alignToZero(Location referLocation) {
        Map<SerializableLocation, String> newBlockStates = new HashMap<>();
        for (Map.Entry<SerializableLocation, String> entry : blockStates.entrySet()) {
            Location currentLocation = entry.getKey().toLocation().subtract(referLocation);
            newBlockStates.put(new SerializableLocation(currentLocation), entry.getValue());
        }
        blockStates.clear();
        blockStates.putAll(newBlockStates);
    }

    public void paste(Location pasteLocation, boolean applyPhysics) {
        World world = pasteLocation.getWorld();
        Location currentLocation = pasteLocation.clone();
        align(pasteLocation);
        for (Map.Entry<SerializableLocation, String> entry : blockStates.entrySet()) {
            BlockData blockState = Bukkit.createBlockData(entry.getValue());
            currentLocation = entry.getKey().toLocation();
            Block block = world.getBlockAt(currentLocation);
            block.setBlockData(blockState, applyPhysics);
            block.getState(applyPhysics).update();
        }
    }

    public void clear() {
        blockStates.clear();
    }

    public void setBlock(Location location, Material material, boolean applyPhysics) {
        Block block = location.getBlock();
        String newState = block.getBlockData().getAsString();
        block.setType(material);
        block.getState().update(true, applyPhysics);
        blockStates.put(new SerializableLocation(location), newState);
    }

    public Map<SerializableLocation, String> getBlockStates() {
        return new HashMap<>(blockStates);
    }

    public String toString() {
        return "Area{" +
                "blockStates=" + blockStates +
                ", attributes=" + attributes +
                '}';
    }

class SerializableLocation implements Serializable {
    @BsonProperty
    private double x, y, z;
    @BsonProperty
    private final String worldName;
    @BsonProperty
    private final String serverName = PluginController.INSTANCE.getServerName();
    @BsonProperty
    private Map<String,String> attributes = new HashMap<>();

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

    public SerializableLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.worldName = location.getWorld().getName();
    }

    public String toString() {
        return "SerializableLocation{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", worldName='" + worldName + '\'' +
                ", serverName='" + serverName + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        SerializableLocation that = (SerializableLocation) obj;
        return x == that.x && y == that.y && z == that.z && worldName.equals(that.worldName);
    }

    @Override
    public int hashCode() {
        long lx = Double.doubleToLongBits(x);
        long ly = Double.doubleToLongBits(y);
        long lz = Double.doubleToLongBits(z);
        int result = (int) (lx ^ (lx >>> 32));
        result = 31 * result + (int) (ly ^ (ly >>> 32));
        result = 31 * result + (int) (lz ^ (lz >>> 32));
        result = 31 * result + worldName.hashCode();
        return result;
    }
}
} 

