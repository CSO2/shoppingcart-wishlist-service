package com.CSO2.shoppingcartwishlistservice.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private String productId;
    private Integer quantity;
}
