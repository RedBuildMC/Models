package fr.elitgaimix.models.spigot.block.area;

import java.util.UUID;

import fr.elitgaimix.models.spigot.block.Area;
import fr.elitgaimix.models.spigot.mongo.repository.MongoDBRepository;

public class AreaRepository extends MongoDBRepository<Area,UUID>{

    public AreaRepository() {
        super(Area.class, "area", "redbuild");
    }
    
}
