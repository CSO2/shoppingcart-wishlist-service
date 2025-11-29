package com.CSO2.shoppingcartwishlistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.CSO2.shoppingcartwishlistservice", excludeName = {
		"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
		"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
@org.springframework.cloud.openfeign.EnableFeignClients(basePackages = "com.CSO2.shoppingcartwishlistservice.client")
public class ShoppingcartWishlistServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingcartWishlistServiceApplication.class, args);
	}

}
