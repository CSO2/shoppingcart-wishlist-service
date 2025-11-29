package com.CSO2.shoppingcartwishlistservice.repository;

import com.CSO2.shoppingcartwishlistservice.entity.SavedBuild;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedBuildRepository extends MongoRepository<SavedBuild, String> {
    List<SavedBuild> findByUserId(String userId);

    List<SavedBuild> findByIsPublicTrue();
}
