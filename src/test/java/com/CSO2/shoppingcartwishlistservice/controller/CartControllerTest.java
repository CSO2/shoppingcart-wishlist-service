package com.CSO2.shoppingcartwishlistservice.controller;

import com.CSO2.shoppingcartwishlistservice.dto.request.AddToCartRequest;
import com.CSO2.shoppingcartwishlistservice.dto.response.CartDTO;
import com.CSO2.shoppingcartwishlistservice.dto.response.CartItemDTO;
import com.CSO2.shoppingcartwishlistservice.service.CartService;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private ObjectMapper objectMapper;

    private CartDTO cartDTO;
    private AddToCartRequest addToCartRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        CartItemDTO item1 = new CartItemDTO("p1", "Product 1", BigDecimal.valueOf(100), 2, "img1",
                BigDecimal.valueOf(200));
        cartDTO = new CartDTO(Arrays.asList(item1), BigDecimal.valueOf(200), 1);

        addToCartRequest = new AddToCartRequest();
        addToCartRequest.setProductId("p1");
        addToCartRequest.setQuantity(2);
    }

    @Test
    void getCart_ShouldReturnCart() throws Exception {
        when(cartService.getCart("user1")).thenReturn(cartDTO);

        mockMvc.perform(get("/api/cart")
                .header("X-User-Subject", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subtotal").value(200))
                .andExpect(jsonPath("$.totalItems").value(1));

        verify(cartService).getCart("user1");
    }

    @Test
    void addItem_ShouldReturnOk() throws Exception {
        doNothing().when(cartService).addItem(eq("user1"), any(AddToCartRequest.class));

        mockMvc.perform(post("/api/cart/items")
                .header("X-User-Subject", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addToCartRequest)))
                .andExpect(status().isOk());

        verify(cartService).addItem(eq("user1"), any(AddToCartRequest.class));
    }

    @Test
    void updateItem_ShouldReturnOk() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("quantity", 5);

        doNothing().when(cartService).updateItem(eq("user1"), eq("p1"), any(AddToCartRequest.class));

        mockMvc.perform(put("/api/cart/items/p1")
                .header("X-User-Subject", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(cartService).updateItem(eq("user1"), eq("p1"), any(AddToCartRequest.class));
    }

    @Test
    void removeItem_ShouldReturnOk() throws Exception {
        doNothing().when(cartService).removeItem("user1", "p1");

        mockMvc.perform(delete("/api/cart/items/p1")
                .header("X-User-Subject", "user1"))
                .andExpect(status().isOk());

        verify(cartService).removeItem("user1", "p1");
    }

    @Test
    void clearCart_ShouldReturnOk() throws Exception {
        doNothing().when(cartService).clearCart("user1");

        mockMvc.perform(delete("/api/cart")
                .header("X-User-Subject", "user1"))
                .andExpect(status().isOk());

        verify(cartService).clearCart("user1");
    }

    @Test
    void clearCartAlt_ShouldReturnOk() throws Exception {
        doNothing().when(cartService).clearCart("user1");

        mockMvc.perform(delete("/api/cart/clear")
                .header("X-User-Subject", "user1"))
                .andExpect(status().isOk());

        verify(cartService).clearCart("user1");
    }
}
