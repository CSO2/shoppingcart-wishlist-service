package com.CSO2.shoppingcartwishlistservice.controller;

import com.CSO2.shoppingcartwishlistservice.dto.AddToCartRequest;
import com.CSO2.shoppingcartwishlistservice.dto.CartDTO;
import com.CSO2.shoppingcartwishlistservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestHeader("X-User-Subject") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(@RequestHeader("X-User-Subject") String userId,
            @RequestBody AddToCartRequest req) {
        cartService.addItem(userId, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(@RequestHeader("X-User-Subject") String userId, @PathVariable String id) {
        cartService.removeItem(userId, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestHeader("X-User-Subject") String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

}
