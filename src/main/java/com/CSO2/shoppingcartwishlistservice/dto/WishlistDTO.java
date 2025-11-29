package com.CSO2.shoppingcartwishlistservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {
    private List<ProductSummary> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSummary {
        private String id;
        private String name;
        private String description;
        private String price;
        private String imageUrl;
    }
}
