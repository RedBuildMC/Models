package fr.redbuild.models.paper.world;

import java.util.UUID;
import fr.redbuild.models.paper.mongo.repository.MongoDBRepository;
import fr.redbuild.models.paper.plugin.PluginController;

public class WorldRepository extends MongoDBRepository<SWorld, UUID>{

    public WorldRepository() {
        super(SWorld.class, "world", PluginController.INSTANCE.getConfig().getString("database.name"));
    }
    
}
