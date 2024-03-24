package fr.elitgaimix.models.spigot.region;

import fr.elitgaimix.models.spigot.mongo.repository.MongoDBRepository;
import fr.elitgaimix.models.spigot.plugin.PluginController;

import java.util.UUID;

public class RegionRepository extends MongoDBRepository<Region, UUID> {
    public RegionRepository() {
        super(Region.class, "region", PluginController.INSTANCE.dbName());
    }
}
