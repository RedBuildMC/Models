package fr.redbuild.models.spigot.block;

import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Location;

import fr.redbuild.models.spigot.utils.injector.Injector;
import fr.redbuild.models.spigot.utils.mongo.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Getter
@Setter
public class Area {
    // https://github.com/Otomny/flow/tree/main/spigot/src/main/java/fr/omny/flow/world
    @BsonId
    @Id
    private UUID uuid = UUID.randomUUID();
    @BsonProperty
    private List<RBlock> blocks = new ArrayList<>();
    @BsonProperty
    private Map<String,String> attributes = new HashMap<>();

    public Area(List<RBlock> blocks, UUID uuid) {
        this.blocks = blocks;
        this.uuid = uuid;
        this.attributes = new HashMap<>();
    }

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

    public Area(Location start,Location end){
        // for(int x = start.getBlockX(); x <= end.getBlockX(); x++){
        //     for(int y = start.getBlockY(); y <= end.getBlockY(); y++){
        //         for(int z = start.getBlockZ(); z <= end.getBlockZ(); z++){
        //             blocks.add(RBlock.fromBlock(start.getWorld().getBlockAt(x,y,z)).alignToZero(start));
        //         }
        //     }
        // }
        var minX = Math.min(start.getBlockX(), end.getBlockX());
		var minY = Math.min(start.getBlockY(), end.getBlockY());
		var minZ = Math.min(start.getBlockZ(), end.getBlockZ());

		var maxX = Math.max(start.getBlockX(), end.getBlockX());
		var maxY = Math.max(start.getBlockY(), end.getBlockY());
		var maxZ = Math.max(start.getBlockZ(), end.getBlockZ());

        Location repository = new Location(start.getWorld(), minX, minY, minZ);

        for (int y = maxY; y >= minY; y--) {
			for (int x = minX; x <= maxX; x++) {
				for (int z = minZ; z <= maxZ; z++) {
					blocks.add(RBlock.fromBlock(start.getWorld().getBlockAt(x,y,z)).alignToZero(repository));
                }
            }
        }
    }


    public void paste(Location location){
        blocks.forEach(block -> {
            System.out.println(location);
            block.align(location);
            System.out.println(location);
        });
        Injector.getInstance(BlockUtils.class).fill(blocks);
    }

}
