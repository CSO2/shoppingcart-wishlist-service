package com.CSO2.shoppingcartwishlistservice.controller;

import com.CSO2.shoppingcartwishlistservice.entity.SavedBuild;
import com.CSO2.shoppingcartwishlistservice.service.SavedBuildService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SavedBuildControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SavedBuildService savedBuildService;

    @InjectMocks
    private SavedBuildController savedBuildController;

    private ObjectMapper objectMapper;

    private SavedBuild savedBuild;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(savedBuildController).build();

        savedBuild = new SavedBuild();
        savedBuild.setId("build1");
        savedBuild.setUserId("user1");
        savedBuild.setName("My Build");
        savedBuild.setTotalPrice(BigDecimal.valueOf(1000));
        savedBuild.setPublic(true);
    }

    @Test
    void getUserBuilds_ShouldReturnBuilds() throws Exception {
        when(savedBuildService.getBuildsByUserId("user1")).thenReturn(Collections.singletonList(savedBuild));

        mockMvc.perform(get("/api/wishlist/builds/user/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("build1"));

        verify(savedBuildService).getBuildsByUserId("user1");
    }

    @Test
    void getPublicBuilds_ShouldReturnBuilds() throws Exception {
        when(savedBuildService.getPublicBuilds()).thenReturn(Collections.singletonList(savedBuild));

        mockMvc.perform(get("/api/wishlist/builds/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("build1"));

        verify(savedBuildService).getPublicBuilds();
    }

    @Test
    void createBuild_ShouldReturnBuild() throws Exception {
        when(savedBuildService.createBuild(any(SavedBuild.class))).thenReturn(savedBuild);

        mockMvc.perform(post("/api/wishlist/builds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedBuild)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("build1"));

        verify(savedBuildService).createBuild(any(SavedBuild.class));
    }

    @Test
    void updateBuild_ShouldReturnBuild() throws Exception {
        when(savedBuildService.updateBuild(eq("build1"), any(SavedBuild.class))).thenReturn(savedBuild);

        mockMvc.perform(put("/api/wishlist/builds/build1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedBuild)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("build1"));

        verify(savedBuildService).updateBuild(eq("build1"), any(SavedBuild.class));
    }

    @Test
    void deleteBuild_ShouldReturnOk() throws Exception {
        doNothing().when(savedBuildService).deleteBuild("build1");

        mockMvc.perform(delete("/api/wishlist/builds/build1"))
                .andExpect(status().isOk());

        verify(savedBuildService).deleteBuild("build1");
    }

    @Test
    void getBuildById_ShouldReturnBuild() throws Exception {
        when(savedBuildService.getBuildById("build1")).thenReturn(Optional.of(savedBuild));

        mockMvc.perform(get("/api/wishlist/builds/build1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("build1"));

        verify(savedBuildService).getBuildById("build1");
    }
}
