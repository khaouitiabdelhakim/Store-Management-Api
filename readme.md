# Store Management Application

A RESTful API for managing store products built with Spring Boot and MySQL.

## Features

- CRUD operations for products
- Search functionality
- Filter by product type and price range
- Pagination support
- Input validation
- Exception handling
- Comprehensive testing

## Technologies Used

- **Framework**: Spring Boot 3.2.0
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Java Version**: 17

## Database Setup

1. Install MySQL and create a database:
```sql
CREATE DATABASE store_db;
USE store_db;

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

2. Update `application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/store_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

1. **Clone and navigate to project directory**
2. **Install dependencies**: `mvn clean install`
3. **Run the application**: `mvn spring-boot:run`
4. **Access the API**: `http://localhost:8080/api/products`

## API Endpoints

### Product Management

#### Get All Products
```http
GET /api/products
```

#### Get All Products (Paginated)
```http
GET /api/products/paginated?page=0&size=10&sortBy=name&sortDir=asc
```

#### Get Product by ID
```http
GET /api/products/{id}
```

#### Create Product
```http
POST /api/products
Content-Type: application/json

{
    "type": "fruit",
    "name": "Apple",
    "description": "Fresh red apples",
    "price": 2.99,
    "imageUrl": "https://example.com/apple.jpg"
}
```

#### Update Product
```http
PUT /api/products/{id}
Content-Type: application/json

{
    "name": "Updated Apple",
    "price": 3.49
}
```

#### Delete Product
```http
DELETE /api/products/{id}
```

### Search and Filter

#### Search Products
```http
GET /api/products/search?q=apple
```

#### Get Products by Type
```http
GET /api/products/type/fruit
```

#### Get Products by Price Range
```http
GET /api/products/price-range?minPrice=1.00&maxPrice=5.00
```

#### Get All Product Types
```http
GET /api/products/types
```

#### Get Product Count by Type
```http
GET /api/products/count/type/fruit
```

## Example Requests with cURL

### Create a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "type": "fruit",
    "name": "Banana",
    "description": "Fresh yellow bananas",
    "price": 1.99,
    "imageUrl": "https://example.com/banana.jpg"
  }'
```

### Get All Products
```bash
curl -X GET http://localhost:8080/api/products
```

### Search Products
```bash
curl -X GET "http://localhost:8080/api/products/search?q=apple"
```

### Update a Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Premium Apple",
    "price": 3.99
  }'
```

### Delete a Product
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

## Response Examples

### Successful Product Creation (201 Created)
```json
{
    "id": 1,
    "type": "fruit",
    "name": "Apple",
    "description": "Fresh red apples",
    "price": 2.99,
    "imageUrl": "https://example.com/apple.jpg",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```

### Product Not Found (404 Not Found)
```json
{
    "errorCode": "PRODUCT_NOT_FOUND",
    "message": "Product not found with id: 999",
    "timestamp": "2024-01-15T10:30:00"
}
```

### Validation Error (400 Bad Request)
```json
{
    "errorCode": "VALIDATION_FAILED",
    "message": "Input validation failed",
    "validationErrors": {
        "name": "Product name is required",
        "price": "Price must be greater than 0"
    },
    "timestamp": "2024-01-15T10:30:00"
}
```

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=ProductServiceTest
```

### Run Integration Tests
```bash
mvn test -Dtest=ProductControllerIntegrationTest
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/store/
│   │   ├── StoreManagementApplication.java
│   │   ├── controller/
│   │   │   └── ProductController.java
│   │   ├── dto/
│   │   │   ├── ProductCreateRequest.java
│   │   │   ├── ProductUpdateRequest.java
│   │   │   └── ProductResponse.java
│   │   ├── exception/
│   │   │   ├── ProductNotFoundException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── model/
│   │   │   └── Product.java
│   │   ├── repository/
│   │   │   └── ProductRepository.java
│   │   └── service/
│   │       └── ProductService.java
│   └── resources/
│       ├── application.properties
│       ├── application-test.properties
│       └── data.sql
└── test/
    └── java/com/example/store/
        ├── controller/
        │   └── ProductControllerIntegrationTest.java
        └── service/
            └── ProductServiceTest.java
```

## Configuration

### application.properties
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/store_db
spring.datasource.username=root
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080
```

## Validation Rules

- **type**: Required, max 50 characters
- **name**: Required, max 100 characters
- **description**: Optional, max 1000 characters
- **price**: Required, must be > 0, format: XXXXXXXX.XX
- **imageUrl**: Optional

## Error Handling

The application provides comprehensive error handling with appropriate HTTP status codes:

- **200 OK**: Successful GET, PUT requests
- **201 Created**: Successful POST requests
- **400 Bad Request**: Validation errors
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Unexpected server errors

## Sample Data

The application includes sample data in `data.sql`:
- Fruits: Apple, Banana
- Vegetables: Carrot, Broccoli
- Dairy: Milk
- Bakery: Bread

## Performance Considerations

- Database indexing on commonly queried fields (type, name)
- Pagination for large result sets
- Connection pooling with HikariCP
- Query optimization with JPA

## Security Notes

For production deployment, consider adding:
- Spring Security for authentication/authorization
- Input sanitization
- Rate limiting
- HTTPS configuration
- Database connection encryption
