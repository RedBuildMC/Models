package fr.redbuild.models.paper.user;

import java.util.UUID;

import fr.redbuild.models.paper.mongo.repository.MongoDBRepository;

public class UserRepository extends MongoDBRepository<User,UUID>{

    public UserRepository() {
        super(User.class, "user", "redbuild");
    }
    
}
