package com.CSO2.shoppingcartwishlistservice.service;

import com.CSO2.shoppingcartwishlistservice.client.CatalogClient;
import com.CSO2.shoppingcartwishlistservice.dto.WishlistDTO;
import com.CSO2.shoppingcartwishlistservice.entity.Wishlist;
import com.CSO2.shoppingcartwishlistservice.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final CatalogClient catalogClient;

    public void toggleWishlist(String userId, String productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElse(new Wishlist(null, userId, new ArrayList<>(), LocalDateTime.now()));

        if (wishlist.getProductIds().contains(productId)) {
            wishlist.getProductIds().remove(productId);
        } else {
            wishlist.getProductIds().add(productId);
        }
        wishlist.setUpdatedAt(LocalDateTime.now());
        wishlistRepository.save(wishlist);
    }

    public WishlistDTO getWishlist(String userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElse(new Wishlist(null, userId, new ArrayList<>(), LocalDateTime.now()));

        List<WishlistDTO.ProductSummary> products = wishlist.getProductIds().stream()
                .map(catalogClient::getProduct)
                .collect(Collectors.toList());

        return new WishlistDTO(products);
    }
}
