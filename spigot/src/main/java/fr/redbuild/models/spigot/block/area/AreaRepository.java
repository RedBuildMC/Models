package fr.redbuild.models.spigot.block.area;

import java.util.UUID;

import fr.redbuild.models.spigot.block.Area;
import fr.redbuild.models.spigot.mongo.repository.MongoDBRepository;

public class AreaRepository extends MongoDBRepository<Area,UUID>{

    public AreaRepository() {
        super(Area.class, "area", "redbuild");
    }
    
}
