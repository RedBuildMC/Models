package fr.redbuild.models.paper.config;

import java.util.UUID;

import fr.redbuild.models.paper.mongo.repository.MongoDBRepository;

public class ConfigRepository extends MongoDBRepository<Config,UUID>{

    public ConfigRepository() {
        super(Config.class, "configfiles", "redbuild");
    }
    
}
