package com.CSO2.shoppingcartwishlistservice.controller;

import com.CSO2.shoppingcartwishlistservice.dto.response.WishlistDTO;
import com.CSO2.shoppingcartwishlistservice.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<WishlistDTO> getWishlist(@RequestHeader("X-User-Subject") String userId) {
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> toggleWishlist(@RequestHeader("X-User-Subject") String userId,
            @PathVariable String productId) {
        wishlistService.toggleWishlist(userId, productId);
        return ResponseEntity.ok().build();
    }

}
