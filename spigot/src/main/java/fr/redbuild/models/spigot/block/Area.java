package fr.redbuild.models.spigot.block;

import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Location;

import fr.redbuild.models.spigot.utils.injector.Injector;
import fr.redbuild.models.spigot.utils.mongo.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Area {
    @BsonId
    @Id
    private UUID uuid = UUID.randomUUID();
    @BsonProperty
    private List<RBlock> blocks;

    public Area(List<RBlock> blocks, UUID uuid) {
        this.blocks = blocks;
        this.uuid = uuid;
    }

    public Area(Location start,Location end){
        for(int x = start.getBlockX(); x <= end.getBlockX(); x++){
            for(int y = start.getBlockY(); y <= end.getBlockY(); y++){
                for(int z = start.getBlockZ(); z <= end.getBlockZ(); z++){
                    blocks.add(RBlock.fromBlock(start.getWorld().getBlockAt(x,y,z)));
                }
            }
        }
    }

    public void paste(Location location){
        blocks.forEach(block -> block.align(location));
        Injector.getInstance(BlockUtils.class).fill(blocks);
    }

}
