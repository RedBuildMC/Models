package fr.redbuild.models.spigot.grade;

import java.util.UUID;

import fr.redbuild.models.spigot.mongo.repository.MongoDBRepository;

public class GradeRepository extends MongoDBRepository<Grade,UUID>{

    public GradeRepository() {
        super(Grade.class, "grade", "redbuild");
    }
    
}
