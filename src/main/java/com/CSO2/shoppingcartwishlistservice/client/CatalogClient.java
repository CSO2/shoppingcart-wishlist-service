package com.CSO2.shoppingcartwishlistservice.client;

import com.CSO2.shoppingcartwishlistservice.dto.WishlistDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-catalogue-service", url = "http://localhost:8082") // Adjust URL as needed
public interface CatalogClient {

    @GetMapping("/api/products/{id}")
    WishlistDTO.ProductSummary getProduct(@PathVariable("id") String id);
}
