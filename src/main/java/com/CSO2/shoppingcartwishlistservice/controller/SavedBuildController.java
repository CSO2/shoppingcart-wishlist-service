package com.CSO2.shoppingcartwishlistservice.controller;

import com.CSO2.shoppingcartwishlistservice.entity.SavedBuild;
import com.CSO2.shoppingcartwishlistservice.service.SavedBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist/builds")
public class SavedBuildController {

    @Autowired
    private SavedBuildService savedBuildService;

    @GetMapping("/user/{userId}")
    public List<SavedBuild> getUserBuilds(@PathVariable String userId) {
        return savedBuildService.getBuildsByUserId(userId);
    }

    @GetMapping("/public")
    public List<SavedBuild> getPublicBuilds() {
        return savedBuildService.getPublicBuilds();
    }

    @PostMapping
    public ResponseEntity<SavedBuild> createBuild(@RequestBody SavedBuild build) {
        return ResponseEntity.ok(savedBuildService.createBuild(build));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavedBuild> updateBuild(@PathVariable String id, @RequestBody SavedBuild build) {
        SavedBuild updatedBuild = savedBuildService.updateBuild(id, build);
        if (updatedBuild != null) {
            return ResponseEntity.ok(updatedBuild);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuild(@PathVariable String id) {
        savedBuildService.deleteBuild(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavedBuild> getBuildById(@PathVariable String id) {
        return savedBuildService.getBuildById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
