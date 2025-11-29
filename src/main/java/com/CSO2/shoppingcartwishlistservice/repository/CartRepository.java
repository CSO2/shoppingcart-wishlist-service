package com.CSO2.shoppingcartwishlistservice.repository;

import com.CSO2.shoppingcartwishlistservice.entity.Cart;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class CartRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "Cart:";

    public CartRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(Cart cart) {
        redisTemplate.opsForValue().set(KEY_PREFIX + cart.getId(), cart, 30, TimeUnit.DAYS);
    }

    public Cart findById(String id) {
        return (Cart) redisTemplate.opsForValue().get(KEY_PREFIX + id);
    }

    public void delete(String id) {
        redisTemplate.delete(KEY_PREFIX + id);
    }
}
