package com.CSO2.shoppingcartwishlistservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "Cart", timeToLive = 2592000) // 30 days in seconds
public class Cart {

    @Id
    private String id; // SessionId or UserId
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal totalValue = BigDecimal.ZERO;
    private LocalDateTime lastUpdated;
}
