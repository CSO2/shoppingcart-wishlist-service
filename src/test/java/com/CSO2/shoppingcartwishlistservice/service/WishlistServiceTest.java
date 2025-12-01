package com.CSO2.shoppingcartwishlistservice.service;

import com.CSO2.shoppingcartwishlistservice.client.CatalogClient;
import com.CSO2.shoppingcartwishlistservice.dto.response.WishlistDTO;
import com.CSO2.shoppingcartwishlistservice.entity.Wishlist;
import com.CSO2.shoppingcartwishlistservice.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private WishlistService wishlistService;

    private Wishlist wishlist;
    private WishlistDTO.ProductSummary productSummary;

    @BeforeEach
    void setUp() {
        wishlist = new Wishlist("w1", "user1", new ArrayList<>(Collections.singletonList("p1")), LocalDateTime.now());
        productSummary = new WishlistDTO.ProductSummary(
                "p1", "Product 1", "Desc", BigDecimal.valueOf(100),
                Collections.singletonList("img1"), 10, "Brand", 4.5, 10);
    }

    @Test
    void getWishlist_ShouldReturnWishlistDTO() {
        when(wishlistRepository.findByUserId("user1")).thenReturn(Optional.of(wishlist));
        when(catalogClient.getProduct("p1")).thenReturn(productSummary);

        WishlistDTO result = wishlistService.getWishlist("user1");

        assertNotNull(result);
        assertEquals(1, result.getProducts().size());
        assertEquals("p1", result.getProducts().get(0).getId());
    }

    @Test
    void getWishlist_ShouldReturnEmptyWishlist_WhenWishlistNotFound() {
        when(wishlistRepository.findByUserId("user1")).thenReturn(Optional.empty());

        WishlistDTO result = wishlistService.getWishlist("user1");

        assertNotNull(result);
        assertTrue(result.getProducts().isEmpty());
    }

    @Test
    void toggleWishlist_ShouldAddItem_WhenItemNotInWishlist() {
        when(wishlistRepository.findByUserId("user1")).thenReturn(Optional.of(wishlist));

        wishlistService.toggleWishlist("user1", "p2");

        assertTrue(wishlist.getProductIds().contains("p2"));
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void toggleWishlist_ShouldRemoveItem_WhenItemInWishlist() {
        when(wishlistRepository.findByUserId("user1")).thenReturn(Optional.of(wishlist));

        wishlistService.toggleWishlist("user1", "p1");

        assertFalse(wishlist.getProductIds().contains("p1"));
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void toggleWishlist_ShouldCreateNewWishlist_WhenWishlistNotFound() {
        when(wishlistRepository.findByUserId("user1")).thenReturn(Optional.empty());

        wishlistService.toggleWishlist("user1", "p1");

        verify(wishlistRepository).save(any(Wishlist.class));
    }
}
