package fr.elitgaimix.models.spigot.world;

import java.util.UUID;
import fr.elitgaimix.models.spigot.mongo.repository.MongoDBRepository;
import fr.elitgaimix.models.spigot.plugin.PluginController;

public class WorldRepository extends MongoDBRepository<SWorld, UUID>{

    public WorldRepository() {
        super(SWorld.class, "world", PluginController.INSTANCE.getConfig().getString("database.name"));
    }
    
}
