package com.CSO2.shoppingcartwishlistservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "saved_builds")
public class SavedBuild {
    @Id
    private String id;
    private String name;
    private String userId;
    private Map<String, BuildComponent> components; // Key: component type (CPU, GPU, etc.), Value: Component details
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SavedBuild() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Inner class for component details to avoid full product duplication if not
    // needed,
    // or we can store minimal info. Frontend sends full product info usually.
    // Let's store minimal info + product ID.
    public static class BuildComponent {
        private String productId;
        private String name;
        private BigDecimal price;
        private String imageUrl;
        private String brand;

        public BuildComponent() {
        }

        public BuildComponent(String productId, String name, BigDecimal price, String imageUrl, String brand) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.brand = brand;
        }

        // Getters and Setters
        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, BuildComponent> getComponents() {
        return components;
    }

    public void setComponents(Map<String, BuildComponent> components) {
        this.components = components;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
