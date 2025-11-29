package com.CSO2.shoppingcartwishlistservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {
    private List<ProductSummary> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductSummary {
        private String id;
        private String name;
        private String description;
        private BigDecimal price;
        private List<String> imageUrls;
        private Integer stockLevel;
        private String brand;
        private Double averageRating;
        private Integer reviewCount;

        // Helper for backward compatibility or ease of use
        public String getImageUrl() {
            return (imageUrls != null && !imageUrls.isEmpty()) ? imageUrls.get(0) : null;
        }
    }
}
