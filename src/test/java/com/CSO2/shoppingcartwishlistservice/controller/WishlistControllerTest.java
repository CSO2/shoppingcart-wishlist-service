package com.CSO2.shoppingcartwishlistservice.controller;

import com.CSO2.shoppingcartwishlistservice.dto.response.WishlistDTO;
import com.CSO2.shoppingcartwishlistservice.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    private WishlistDTO wishlistDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(wishlistController).build();

        WishlistDTO.ProductSummary product = new WishlistDTO.ProductSummary(
                "p1", "Product 1", "Desc", BigDecimal.valueOf(100),
                Collections.singletonList("img1"), 10, "Brand", 4.5, 10);
        wishlistDTO = new WishlistDTO(Collections.singletonList(product));
    }

    @Test
    void getWishlist_ShouldReturnWishlist() throws Exception {
        when(wishlistService.getWishlist("user1")).thenReturn(wishlistDTO);

        mockMvc.perform(get("/api/wishlist")
                .header("X-User-Subject", "user1"))
                .andExpect(status().isOk());

        verify(wishlistService).getWishlist("user1");
    }

    @Test
    void toggleWishlist_ShouldReturnOk() throws Exception {
        doNothing().when(wishlistService).toggleWishlist("user1", "p1");

        mockMvc.perform(post("/api/wishlist/p1")
                .header("X-User-Subject", "user1"))
                .andExpect(status().isOk());

        verify(wishlistService).toggleWishlist("user1", "p1");
    }
}
