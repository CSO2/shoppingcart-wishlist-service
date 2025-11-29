package com.CSO2.shoppingcartwishlistservice.service;

import com.CSO2.shoppingcartwishlistservice.client.CatalogClient;
import com.CSO2.shoppingcartwishlistservice.dto.request.AddToCartRequest;
import com.CSO2.shoppingcartwishlistservice.dto.response.CartDTO;
import com.CSO2.shoppingcartwishlistservice.dto.response.CartItemDTO;
import com.CSO2.shoppingcartwishlistservice.dto.response.WishlistDTO;
import com.CSO2.shoppingcartwishlistservice.entity.Cart;
import com.CSO2.shoppingcartwishlistservice.entity.CartItem;
import com.CSO2.shoppingcartwishlistservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CatalogClient catalogClient;

    public CartDTO getCart(String userId) {
        Cart cart = cartRepository.findById(userId);
        if (cart == null) {
            return new CartDTO(new ArrayList<>(), BigDecimal.ZERO, 0);
        }
        return mapToDTO(cart);
    }

    public void addItem(String userId, AddToCartRequest req) {
        Cart cart = cartRepository.findById(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setId(userId);
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(req.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + req.getQuantity());
        } else {
            WishlistDTO.ProductSummary product = catalogClient.getProduct(req.getProductId());
            CartItem newItem = new CartItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    req.getQuantity(),
                    product.getImageUrl());
            cart.getItems().add(newItem);
        }

        updateCartTotals(cart);
        cartRepository.save(cart);
    }

    public void updateItem(String userId, String productId, AddToCartRequest req) {
        Cart cart = cartRepository.findById(userId);
        if (cart != null) {
            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();
            
            if (existingItem.isPresent()) {
                existingItem.get().setQuantity(req.getQuantity());
                updateCartTotals(cart);
                cartRepository.save(cart);
            }
        }
    }

    public void removeItem(String userId, String productId) {
        Cart cart = cartRepository.findById(userId);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getProductId().equals(productId));
            updateCartTotals(cart);
            cartRepository.save(cart);
        }
    }

    public void clearCart(String userId) {
        cartRepository.delete(userId);
    }

    private void updateCartTotals(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalValue(total);
        cart.setLastUpdated(LocalDateTime.now());
    }

    private CartDTO mapToDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProductId(),
                        item.getProductName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getImageUrl(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))))
                .collect(Collectors.toList());

        return new CartDTO(itemDTOs, cart.getTotalValue(), cart.getItems().size());
    }
}
