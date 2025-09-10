package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUserName(String userName);

    User deleteByUserName(String userName);
}
