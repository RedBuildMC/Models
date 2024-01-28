package fr.redbuild.models.spigot.world;

import java.util.UUID;
import fr.redbuild.models.spigot.mongo.repository.MongoDBRepository;
import fr.redbuild.models.spigot.plugin.PluginController;

public class WorldRepository extends MongoDBRepository<SWorld, UUID>{

    public WorldRepository() {
        super(SWorld.class, "world", PluginController.INSTANCE.getConfig().getString("database.name"));
    }
    
}
