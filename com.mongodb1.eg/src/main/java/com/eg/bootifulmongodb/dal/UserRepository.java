package com.eg.bootifulmongodb.dal;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eg.bootifulmongodb.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
