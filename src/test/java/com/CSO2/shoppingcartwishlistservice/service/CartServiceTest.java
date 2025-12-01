package com.CSO2.shoppingcartwishlistservice.service;

import com.CSO2.shoppingcartwishlistservice.client.CatalogClient;
import com.CSO2.shoppingcartwishlistservice.dto.request.AddToCartRequest;
import com.CSO2.shoppingcartwishlistservice.dto.response.CartDTO;
import com.CSO2.shoppingcartwishlistservice.dto.response.WishlistDTO;
import com.CSO2.shoppingcartwishlistservice.entity.Cart;
import com.CSO2.shoppingcartwishlistservice.entity.CartItem;
import com.CSO2.shoppingcartwishlistservice.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private CartItem cartItem;
    private WishlistDTO.ProductSummary productSummary;

    @BeforeEach
    void setUp() {
        cartItem = new CartItem("p1", "Product 1", BigDecimal.valueOf(100), 1, "img1");
        cart = new Cart();
        cart.setId("user1");
        cart.setItems(new ArrayList<>(Collections.singletonList(cartItem)));
        cart.setTotalValue(BigDecimal.valueOf(100));

        productSummary = new WishlistDTO.ProductSummary(
                "p1", "Product 1", "Desc", BigDecimal.valueOf(100),
                Collections.singletonList("img1"), 10, "Brand", 4.5, 10);
    }

    @Test
    void getCart_ShouldReturnCartDTO() {
        when(cartRepository.findById("user1")).thenReturn(cart);

        CartDTO result = cartService.getCart("user1");

        assertNotNull(result);
        assertEquals(1, result.getTotalItems());
        assertEquals(BigDecimal.valueOf(100), result.getSubtotal());
    }

    @Test
    void getCart_ShouldReturnEmptyCart_WhenCartNotFound() {
        when(cartRepository.findById("user1")).thenReturn(null);

        CartDTO result = cartService.getCart("user1");

        assertNotNull(result);
        assertEquals(0, result.getTotalItems());
        assertEquals(BigDecimal.ZERO, result.getSubtotal());
    }

    @Test
    void addItem_ShouldAddNewItem_WhenItemNotInCart() {
        when(cartRepository.findById("user1")).thenReturn(cart);
        when(catalogClient.getProduct("p2")).thenReturn(productSummary);

        AddToCartRequest req = new AddToCartRequest();
        req.setProductId("p2");
        req.setQuantity(1);

        cartService.addItem("user1", req);

        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addItem_ShouldUpdateQuantity_WhenItemInCart() {
        when(cartRepository.findById("user1")).thenReturn(cart);

        AddToCartRequest req = new AddToCartRequest();
        req.setProductId("p1");
        req.setQuantity(1);

        cartService.addItem("user1", req);

        assertEquals(2, cart.getItems().get(0).getQuantity());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void updateItem_ShouldUpdateQuantity() {
        when(cartRepository.findById("user1")).thenReturn(cart);

        AddToCartRequest req = new AddToCartRequest();
        req.setProductId("p1");
        req.setQuantity(5);

        cartService.updateItem("user1", "p1", req);

        assertEquals(5, cart.getItems().get(0).getQuantity());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void removeItem_ShouldRemoveItem() {
        when(cartRepository.findById("user1")).thenReturn(cart);

        cartService.removeItem("user1", "p1");

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void clearCart_ShouldDeleteCart() {
        cartService.clearCart("user1");
        verify(cartRepository).delete("user1");
    }
}
