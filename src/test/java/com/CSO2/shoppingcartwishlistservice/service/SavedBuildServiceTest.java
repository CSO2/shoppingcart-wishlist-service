package com.CSO2.shoppingcartwishlistservice.service;

import com.CSO2.shoppingcartwishlistservice.entity.SavedBuild;
import com.CSO2.shoppingcartwishlistservice.repository.SavedBuildRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavedBuildServiceTest {

    @Mock
    private SavedBuildRepository savedBuildRepository;

    @InjectMocks
    private SavedBuildService savedBuildService;

    private SavedBuild savedBuild;

    @BeforeEach
    void setUp() {
        savedBuild = new SavedBuild();
        savedBuild.setId("build1");
        savedBuild.setUserId("user1");
        savedBuild.setName("My Build");
        savedBuild.setTotalPrice(BigDecimal.valueOf(1000));
        savedBuild.setPublic(true);
    }

    @Test
    void getBuildsByUserId_ShouldReturnBuilds() {
        when(savedBuildRepository.findByUserId("user1")).thenReturn(Collections.singletonList(savedBuild));

        List<SavedBuild> result = savedBuildService.getBuildsByUserId("user1");

        assertFalse(result.isEmpty());
        assertEquals("build1", result.get(0).getId());
    }

    @Test
    void getPublicBuilds_ShouldReturnBuilds() {
        when(savedBuildRepository.findByIsPublicTrue()).thenReturn(Collections.singletonList(savedBuild));

        List<SavedBuild> result = savedBuildService.getPublicBuilds();

        assertFalse(result.isEmpty());
        assertEquals("build1", result.get(0).getId());
    }

    @Test
    void createBuild_ShouldReturnBuild() {
        when(savedBuildRepository.save(any(SavedBuild.class))).thenReturn(savedBuild);

        SavedBuild result = savedBuildService.createBuild(savedBuild);

        assertNotNull(result);
        assertEquals("build1", result.getId());
    }

    @Test
    void updateBuild_ShouldReturnUpdatedBuild() {
        when(savedBuildRepository.findById("build1")).thenReturn(Optional.of(savedBuild));
        when(savedBuildRepository.save(any(SavedBuild.class))).thenReturn(savedBuild);

        SavedBuild updatedDetails = new SavedBuild();
        updatedDetails.setName("Updated Build");

        SavedBuild result = savedBuildService.updateBuild("build1", updatedDetails);

        assertNotNull(result);
        assertEquals("Updated Build", result.getName());
    }

    @Test
    void updateBuild_ShouldReturnNull_WhenBuildNotFound() {
        when(savedBuildRepository.findById("build1")).thenReturn(Optional.empty());

        SavedBuild result = savedBuildService.updateBuild("build1", new SavedBuild());

        assertNull(result);
    }

    @Test
    void deleteBuild_ShouldDeleteBuild() {
        savedBuildService.deleteBuild("build1");
        verify(savedBuildRepository).deleteById("build1");
    }

    @Test
    void getBuildById_ShouldReturnBuild() {
        when(savedBuildRepository.findById("build1")).thenReturn(Optional.of(savedBuild));

        Optional<SavedBuild> result = savedBuildService.getBuildById("build1");

        assertTrue(result.isPresent());
        assertEquals("build1", result.get().getId());
    }
}
