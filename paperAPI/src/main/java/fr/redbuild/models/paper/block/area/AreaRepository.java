package fr.redbuild.models.paper.block.area;

import java.util.UUID;

import fr.redbuild.models.paper.block.Area;
import fr.redbuild.models.paper.mongo.repository.MongoDBRepository;

public class AreaRepository extends MongoDBRepository<Area,UUID>{

    public AreaRepository() {
        super(Area.class, "area", "redbuild");
    }
    
}
