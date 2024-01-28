package fr.redbuild.models.spigot.block;

import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import fr.redbuild.models.spigot.utils.mongo.Id;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
public class RBlock {
    @BsonId
    @Id
    private UUID uuid = UUID.randomUUID();
    @BsonProperty
    private Location location;
    @BsonProperty
    private Material material;
    @BsonProperty
    private BlockData data;
    
    public RBlock(Location location,UUID uuid,Material material,BlockData data) {
        this.location = location;
        this.uuid = uuid;
        this.material = material;
        this.data = data;
    }

    public RBlock(Location location) {
        this.location = location;
    }

    public static RBlock fromBlock(Block block) {
        return new RBlock(block.getLocation(),UUID.randomUUID(),block.getType(),block.getBlockData());
    }

    public Block toBlock() {
        return location.getBlock();
    }
    
    public Location compareLocation(RBlock block) {
        return location.subtract(block.getLocation());
    }

    public Location compareLocation(Location location) {
        return this.location.subtract(location);
    }

    public Location compareLocation(Block block) {
        return location.subtract(block.getLocation());
    }

    public void align(RBlock block) {
        location = location.add(block.compareLocation(location));
    }

    public void align(Location location) {
        this.location = this.location.add(compareLocation(location));
    }

    public void alignToZero(Location repository) {
        location = location.add(repository.subtract(location));
    }
}
