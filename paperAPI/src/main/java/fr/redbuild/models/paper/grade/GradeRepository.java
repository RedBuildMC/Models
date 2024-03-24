package fr.redbuild.models.paper.grade;

import java.util.UUID;

import fr.redbuild.models.paper.mongo.repository.MongoDBRepository;

public class GradeRepository extends MongoDBRepository<Grade,UUID>{

    public GradeRepository() {
        super(Grade.class, "grade", "redbuild");
    }
    
}
