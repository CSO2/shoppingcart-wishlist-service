package com.CSO2.shoppingcartwishlistservice.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private String productId;
    private Integer quantity;
}
