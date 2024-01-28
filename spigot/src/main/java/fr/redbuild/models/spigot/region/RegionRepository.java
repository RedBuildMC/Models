package fr.redbuild.models.spigot.region;

import fr.redbuild.models.spigot.mongo.repository.MongoDBRepository;
import fr.redbuild.models.spigot.plugin.PluginController;

import java.util.UUID;

public class RegionRepository extends MongoDBRepository<Region, UUID> {
    public RegionRepository() {
        super(Region.class, "region", PluginController.INSTANCE.dbName());
    }
}
