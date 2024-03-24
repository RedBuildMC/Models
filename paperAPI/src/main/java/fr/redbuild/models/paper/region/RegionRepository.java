package fr.redbuild.models.paper.region;

import fr.redbuild.models.paper.mongo.repository.MongoDBRepository;
import fr.redbuild.models.paper.plugin.PluginController;

import java.util.UUID;

public class RegionRepository extends MongoDBRepository<Region, UUID> {
    public RegionRepository() {
        super(Region.class, "region", PluginController.INSTANCE.dbName());
    }
}
