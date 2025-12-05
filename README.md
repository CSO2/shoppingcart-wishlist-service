# 🛒 Shopping Cart & Wishlist Service

> Shopping cart management, wishlists, and saved PC builds for the CS02 E-Commerce Platform

## 📋 Overview

The Shopping Cart & Wishlist Service manages user shopping carts, wishlists, and saved PC builds. It uses Redis for high-performance cart operations and MongoDB for persistent wishlist and build storage.

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 4.0.0 |
| Cart Storage | Redis | 7 |
| Wishlist/Builds Storage | MongoDB | Latest |
| Service Communication | Spring Cloud OpenFeign | Latest |
| Build Tool | Maven | 3.x |

## 🚀 API Endpoints

### Shopping Cart

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/cart` | Yes | Get user's cart |
| `POST` | `/api/cart/items` | Yes | Add item to cart |
| `PUT` | `/api/cart/items/{productId}` | Yes | Update cart item quantity |
| `DELETE` | `/api/cart/items/{productId}` | Yes | Remove item from cart |
| `DELETE` | `/api/cart` | Yes | Clear entire cart |
| `DELETE` | `/api/cart/clear` | Yes | Clear cart (alternative) |
| `GET` | `/api/cart/count` | Yes | Get cart item count |
| `POST` | `/api/cart/validate` | Yes | Validate cart stock |

### Wishlist

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/wishlist` | Yes | Get user's wishlist |
| `POST` | `/api/wishlist/toggle` | Yes | Toggle product in wishlist |
| `POST` | `/api/wishlist/items` | Yes | Add to wishlist |
| `DELETE` | `/api/wishlist/items/{productId}` | Yes | Remove from wishlist |
| `GET` | `/api/wishlist/check/{productId}` | Yes | Check if product in wishlist |

### Saved PC Builds

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/wishlist/builds` | Yes | Get user's saved builds |
| `GET` | `/api/wishlist/builds/public` | No | Get public builds |
| `POST` | `/api/wishlist/builds` | Yes | Create saved build |
| `PUT` | `/api/wishlist/builds/{id}` | Yes | Update saved build |
| `DELETE` | `/api/wishlist/builds/{id}` | Yes | Delete saved build |
| `GET` | `/api/wishlist/builds/{id}` | Yes | Get build by ID |
| `PUT` | `/api/wishlist/builds/{id}/visibility` | Yes | Toggle build visibility |

## 📊 Data Models

### Cart (Redis)

```java
{
  "userId": "uuid",
  "items": [
    {
      "productId": "string",
      "productName": "NVIDIA RTX 4090",
      "price": 1599.99,
      "quantity": 1,
      "imageUrl": "string",
      "stockLevel": 25
    }
  ],
  "subtotal": 1599.99,
  "itemCount": 1,
  "updatedAt": "datetime"
}
```

### CartItem

```java
{
  "productId": "string",
  "productName": "string",
  "price": 599.99,
  "quantity": 2,
  "imageUrl": "string",
  "stockLevel": 10,
  "maxQuantity": 10
}
```

### Wishlist (MongoDB)

```java
{
  "id": "string",
  "userId": "uuid",
  "products": [
    {
      "productId": "string",
      "addedAt": "datetime"
    }
  ],
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### SavedBuild (MongoDB)

```java
{
  "id": "string",
  "userId": "uuid",
  "name": "Ultimate Gaming Build",
  "description": "High-end gaming PC for 4K gaming",
  "components": {
    "cpu": {
      "productId": "string",
      "name": "Intel Core i9-14900K",
      "price": 589.99
    },
    "gpu": {
      "productId": "string",
      "name": "NVIDIA RTX 4090",
      "price": 1599.99
    },
    "motherboard": { ... },
    "memory": { ... },
    "storage": { ... },
    "psu": { ... },
    "case": { ... },
    "cooling": { ... }
  },
  "totalPrice": 4567.89,
  "isPublic": true,
  "likes": 42,
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

## 🔧 Configuration

### Application Properties

```yaml
server:
  port: 8084

spring:
  data:
    redis:
      host: localhost
      port: 6379
    mongodb:
      uri: mongodb://localhost:27017/CSO2_shoppingcart_wishlist_service

feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000

catalog-service:
  url: http://localhost:8082
```

### Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `SPRING_DATA_REDIS_HOST` | No | `localhost` | Redis host |
| `SPRING_DATA_REDIS_PORT` | No | `6379` | Redis port |
| `SPRING_DATA_MONGODB_URI` | No | `mongodb://localhost:27017` | MongoDB URI |
| `CATALOG_SERVICE_URL` | No | `http://localhost:8082` | Product service URL |
| `SERVER_PORT` | No | `8084` | Service port |

## 📦 Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

## 🔌 Feign Clients

### CatalogServiceClient

```java
@FeignClient(name = "catalog-service", url = "${catalog-service.url}")
public interface CatalogServiceClient {
    @GetMapping("/api/products/{id}")
    ProductDTO getProduct(@PathVariable String id);
    
    @GetMapping("/api/products")
    List<ProductDTO> getProducts(@RequestParam List<String> ids);
}
```

## 🏃 Running the Service

### Local Development

```bash
cd backend/shoppingcart-wishlist-service

# Using Maven Wrapper
./mvnw spring-boot:run

# Or with Maven
mvn spring-boot:run
```

### Docker

```bash
cd backend/shoppingcart-wishlist-service

# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t cs02/cart-wishlist-service .

# Run container
docker run -p 8084:8084 \
  -e SPRING_DATA_REDIS_HOST=redis \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/CSO2_shoppingcart_wishlist_service \
  -e CATALOG_SERVICE_URL=http://product-catalogue-service:8082 \
  cs02/cart-wishlist-service
```

## 🗄️ Database Requirements

### Redis
- Running on port `6379`
- Used for cart session storage
- Data structure: Hash with user ID as key

### MongoDB
- Running on port `27017`
- Database: `CSO2_shoppingcart_wishlist_service`
- Collections: `wishlists`, `saved_builds`

## ✅ Features - Completion Status

| Feature | Status | Notes |
|---------|--------|-------|
| Cart CRUD | ✅ Complete | Add, update, remove items |
| Cart persistence (Redis) | ✅ Complete | Session-based storage |
| Cart item count | ✅ Complete | Quick count endpoint |
| Stock validation | ✅ Complete | Pre-checkout validation |
| Wishlist toggle | ✅ Complete | Add/remove with one action |
| Wishlist persistence | ✅ Complete | MongoDB storage |
| Saved PC builds | ✅ Complete | Full CRUD |
| Public build sharing | ✅ Complete | Gallery display |
| Build visibility toggle | ✅ Complete | Public/private |
| Product enrichment (Feign) | ✅ Complete | Real-time product data |
| Cart clearing | ✅ Complete | After checkout |

### **Overall Completion: 95%** ✅

## ❌ Not Implemented / Future Enhancements

| Feature | Priority | Notes |
|---------|----------|-------|
| Cart expiration | Low | Auto-expire abandoned carts |
| Wishlist notifications | Medium | Back-in-stock alerts |
| Build likes/favorites | Low | Community engagement |
| Build comments | Low | Community features |
| Cart merge (guest to user) | Medium | After login |
| Save cart for later | Low | Named cart saves |
| Price drop alerts | Low | Wishlist price tracking |

## 📁 Project Structure

```
shoppingcart-wishlist-service/
├── src/
│   ├── main/
│   │   ├── java/com/cs02/cart/
│   │   │   ├── CartWishlistApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── CartController.java
│   │   │   │   ├── WishlistController.java
│   │   │   │   └── SavedBuildController.java
│   │   │   ├── model/
│   │   │   │   ├── Cart.java
│   │   │   │   ├── CartItem.java
│   │   │   │   ├── Wishlist.java
│   │   │   │   └── SavedBuild.java
│   │   │   ├── repository/
│   │   │   │   ├── CartRepository.java
│   │   │   │   ├── WishlistRepository.java
│   │   │   │   └── SavedBuildRepository.java
│   │   │   ├── service/
│   │   │   │   ├── CartService.java
│   │   │   │   ├── WishlistService.java
│   │   │   │   └── SavedBuildService.java
│   │   │   └── client/
│   │   │       └── CatalogServiceClient.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── pom.xml
├── Dockerfile
└── README.md
```

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Test cart endpoints
curl -H "X-User-Id: user123" http://localhost:8084/api/cart
curl -X POST -H "X-User-Id: user123" -H "Content-Type: application/json" \
  http://localhost:8084/api/cart/items \
  -d '{"productId": "prod123", "quantity": 2}'

# Test wishlist endpoints
curl -H "X-User-Id: user123" http://localhost:8084/api/wishlist
curl -X POST -H "X-User-Id: user123" -H "Content-Type: application/json" \
  http://localhost:8084/api/wishlist/toggle \
  -d '{"productId": "prod123"}'

# Test saved builds
curl http://localhost:8084/api/wishlist/builds/public
curl -H "X-User-Id: user123" http://localhost:8084/api/wishlist/builds
```

## 🔗 Related Services

- [API Gateway](../api-gateway/README.md) - Routes `/api/cart/*` and `/api/wishlist/*`
- [Product Catalogue Service](../product-catalogue-service/README.md) - Product data enrichment
- [Order Service](../order-service/README.md) - Cart-to-order conversion

## 📝 Notes

- Service runs on port **8084**
- **Redis** is used for cart (high-performance, session-based)
- **MongoDB** is used for wishlists and builds (persistent)
- Cart items are enriched with real-time product data via Feign
- Stock levels are validated before adding to cart
- Cart is cleared automatically after successful checkout
- Saved builds can be shared publicly in the gallery
