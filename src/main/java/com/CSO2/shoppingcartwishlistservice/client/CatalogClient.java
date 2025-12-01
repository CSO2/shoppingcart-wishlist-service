package com.CSO2.shoppingcartwishlistservice.client;

import com.CSO2.shoppingcartwishlistservice.dto.response.WishlistDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-catalogue-service", url = "${catalog.service.url:http://localhost:8082}")
public interface CatalogClient {

    @GetMapping("/api/products/{id}")
    WishlistDTO.ProductSummary getProduct(@PathVariable("id") String id);
}
