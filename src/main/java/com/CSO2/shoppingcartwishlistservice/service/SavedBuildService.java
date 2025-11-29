package com.CSO2.shoppingcartwishlistservice.service;

import com.CSO2.shoppingcartwishlistservice.entity.SavedBuild;
import com.CSO2.shoppingcartwishlistservice.repository.SavedBuildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SavedBuildService {

    @Autowired
    private SavedBuildRepository savedBuildRepository;

    public List<SavedBuild> getBuildsByUserId(String userId) {
        return savedBuildRepository.findByUserId(userId);
    }

    public SavedBuild createBuild(SavedBuild build) {
        build.setCreatedAt(LocalDateTime.now());
        build.setUpdatedAt(LocalDateTime.now());
        return savedBuildRepository.save(build);
    }

    public SavedBuild updateBuild(String id, SavedBuild buildDetails) {
        Optional<SavedBuild> buildOptional = savedBuildRepository.findById(id);
        if (buildOptional.isPresent()) {
            SavedBuild build = buildOptional.get();
            build.setName(buildDetails.getName());
            build.setComponents(buildDetails.getComponents());
            build.setTotalPrice(buildDetails.getTotalPrice());
            build.setUpdatedAt(LocalDateTime.now());
            return savedBuildRepository.save(build);
        }
        return null;
    }

    public void deleteBuild(String id) {
        savedBuildRepository.deleteById(id);
    }

    public Optional<SavedBuild> getBuildById(String id) {
        return savedBuildRepository.findById(id);
    }
}
