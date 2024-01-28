package fr.redbuild.models.spigot.user;

import java.util.UUID;

import fr.redbuild.models.spigot.mongo.repository.MongoDBRepository;

public class UserRepository extends MongoDBRepository<User,UUID>{

    public UserRepository() {
        super(User.class, "user", "redbuild");
    }
    
}
