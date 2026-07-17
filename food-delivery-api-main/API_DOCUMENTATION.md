# Food Delivery API - Complete API Documentation

> Comprehensive reference for all REST API endpoints, request/response schemas, authentication, and error handling.

---

## Table of Contents

- [Base URL](#base-url)
- [Authentication](#authentication)
- [Rate Limiting](#rate-limiting)
- [Error Handling](#error-handling)
- [Response Format](#response-format)
- [Users API](#users-api)
- [Restaurants API](#restaurants-api)
- [Menu Items API](#menu-items-api)
- [Menu Item Photos API](#menu-item-photos-api)
- [Orders API](#orders-api)
- [Delivery Partners API](#delivery-partners-api)
- [AI Chatbot API](#ai-chatbot-api)
- [Health & Monitoring API](#health--monitoring-api)

---

## Base URL

| Environment | URL |
|-------------|-----|
| Local Development | `http://localhost:8080` |
| Docker | `http://localhost:8080` |
| Production | `https://api.your-domain.com` |

---

## Authentication

The API uses **JWT (JSON Web Token)** based authentication via Spring Security.

### Obtaining a Token

```bash
curl -X POST http://localhost:8080/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "your_password"
  }'
```

### Using the Token

Include the JWT in the `Authorization` header for all protected endpoints:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

### Token Properties

| Property | Value |
|----------|-------|
| Algorithm | HS256 (HMAC-SHA256) |
| Expiration | Configurable (default 24h) |
| Issuer | `food-delivery-api` |
| Library | jjwt 0.12.6 |

---

## Rate Limiting

| Tier | Requests/Minute | Burst |
|------|----------------|-------|
| Anonymous | 30 | 10 |
| Authenticated | 120 | 30 |
| AI Endpoints | 20 | 5 |

When rate limited, the API returns:

```json
{
  "status": 429,
  "message": "Too Many Requests",
  "retry_after_seconds": 30
}
```

---

## Error Handling

### Standard Error Response

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:30:00Z",
  "path": "/v1/users"
}
```

### HTTP Status Codes

| Code | Meaning | When Used |
|------|---------|-----------|
| `200` | OK | Successful request |
| `201` | Created | Resource created |
| `400` | Bad Request | Validation error, malformed JSON |
| `401` | Unauthorized | Missing or invalid JWT |
| `403` | Forbidden | Insufficient permissions |
| `404` | Not Found | Resource does not exist |
| `409` | Conflict | Duplicate resource (e.g., username) |
| `413` | Payload Too Large | File upload exceeds 5MB |
| `429` | Too Many Requests | Rate limit exceeded |
| `500` | Internal Server Error | Unexpected server failure |

### Validation Errors

```json
{
  "status": 400,
  "message": "Bad Request",
  "errors": [
    {
      "field": "username",
      "message": "Username is required"
    },
    {
      "field": "email",
      "message": "Must be a valid email address"
    }
  ]
}
```

---

## Response Format

All successful responses follow this envelope:

```json
{
  "status": "success",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

For list responses:

```json
{
  "status": "success",
  "data": [ ... ],
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

## Users API

### Create User

`POST /v1/users`

Creates a new user account with optional device registration.

**Request Body:**

```json
{
  "username": "john_doe",
  "first_name": "John",
  "last_name": "Doe",
  "gender": "Male",
  "dob": "1990-05-15",
  "email": "john@example.com",
  "phone_number": "+855123456789",
  "address": "123 Main St, Phnom Penh",
  "user_type": "CUSTOMER",
  "status": "ACTIVE",
  "device_info": {
    "device_name": "iPhone 15",
    "device_token": "fcm_token_abc123",
    "device_type": "IOS"
  }
}
```

**Response (200):**

```json
{
  "status": "success",
  "data": {
    "id": 1,
    "username": "john_doe",
    "first_name": "John",
    "last_name": "Doe",
    "gender": "Male",
    "date_of_birth": "1990-05-15",
    "email": "john@example.com",
    "phone_number": "+855123456789",
    "address": "123 Main St, Phnom Penh",
    "user_type": "CUSTOMER",
    "status": "ACTIVE"
  }
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/v1/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "username": "john_doe",
    "first_name": "John",
    "last_name": "Doe",
    "gender": "Male",
    "dob": "1990-05-15",
    "email": "john@example.com",
    "phone_number": "+855123456789",
    "address": "123 Main St, Phnom Penh",
    "user_type": "CUSTOMER",
    "status": "ACTIVE"
  }'
```

### Update User

`PUT /v1/users/{id}`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | User ID |

**Request Body:** Same as Create User

**cURL:**

```bash
curl -X PUT http://localhost:8080/v1/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "username": "john_doe_updated",
    "first_name": "John",
    "last_name": "Doe",
    "phone_number": "+855987654321",
    "address": "456 New St, Phnom Penh"
  }'
```

### Get User by ID

`GET /v1/users/{id}`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | User ID |

**cURL:**

```bash
curl http://localhost:8080/v1/users/1 \
  -H "Authorization: Bearer <token>"
```

### List All Users

`GET /v1/users`

**cURL:**

```bash
curl http://localhost:8080/v1/users \
  -H "Authorization: Bearer <token>"
```

### Delete User

`DELETE /v1/users/{id}`

**Response:** `200 OK` (empty body)

**cURL:**

```bash
curl -X DELETE http://localhost:8080/v1/users/1 \
  -H "Authorization: Bearer <token>"
```

### User Types

| Type | Description |
|------|-------------|
| `CUSTOMER` | Regular ordering user |
| `RESTAURANT_OWNER` | Restaurant manager |
| `DELIVERY_PARTNER` | Delivery personnel |
| `ADMIN` | System administrator |

---

## Restaurants API

### Create Restaurant

`POST /v1/restaurants`

**Request Body:**

```json
{
  "code": "REST001",
  "name": "Golden Dragon",
  "category": "Chinese",
  "description": "Authentic Chinese cuisine",
  "rating": 4.5,
  "address": "789 Food Street, Phnom Penh",
  "phone_number": "+855111222333",
  "logo_url": "https://cdn.example.com/logos/golden-dragon.png",
  "open_time": "08:00",
  "close_time": "22:00"
}
```

**Response (200):**

```json
{
  "status": "success",
  "data": {
    "id": 1,
    "code": "REST001",
    "name": "Golden Dragon",
    "category": "Chinese",
    "description": "Authentic Chinese cuisine",
    "rating": 4.5,
    "address": "789 Food Street, Phnom Penh",
    "phone_number": "+855111222333",
    "logo_url": "https://cdn.example.com/logos/golden-dragon.png",
    "open_time": "08:00:00",
    "close_time": "22:00:00"
  }
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/v1/restaurants \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "code": "REST001",
    "name": "Golden Dragon",
    "category": "Chinese",
    "description": "Authentic Chinese cuisine",
    "rating": 4.5,
    "address": "789 Food Street",
    "phone_number": "+855111222333",
    "open_time": "08:00",
    "close_time": "22:00"
  }'
```

### Update Restaurant

`PUT /v1/restaurants/{id}`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Restaurant ID |

### Get Restaurant by ID

`GET /v1/restaurants/{id}`

**cURL:**

```bash
curl http://localhost:8080/v1/restaurants/1 \
  -H "Authorization: Bearer <token>"
```

### List All Restaurants

`GET /v1/restaurants`

**cURL:**

```bash
curl http://localhost:8080/v1/restaurants \
  -H "Authorization: Bearer <token>"
```

### Delete Restaurant

`DELETE /v1/restaurants/{id}`

**Response:** `200 OK` (empty body)

---

## Menu Items API

### Create Menu Item

`POST /v1/menu-item`

**Request Body:**

```json
{
  "name": "Kung Pao Chicken",
  "description": "Spicy stir-fried chicken with peanuts",
  "price": 12.99,
  "category": "Main Course",
  "restaurant_id": 1,
  "available": true
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/v1/menu-item \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Kung Pao Chicken",
    "description": "Spicy stir-fried chicken with peanuts",
    "price": 12.99,
    "category": "Main Course",
    "restaurant_id": 1,
    "available": true
  }'
```

### Update Menu Item

`PUT /v1/menu-item/{id}`

### Get Menu Item by ID

`GET /v1/menu-item/{id}`

### List All Menu Items

`GET /v1/menu-item`

### Delete Menu Item

`DELETE /v1/menu-item/{id}`

---

## Menu Item Photos API

### Upload Photos

`POST /v1/menu-item/photo/upload`

Upload one or more images for a menu item. Accepts multipart form data.

**Content-Type:** `multipart/form-data`

**Form Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `files` | File[] | Yes | Image files to upload |
| `menu_item_id` | Long | Yes | Associated menu item |
| `description` | String | No | Photo description |

**Constraints:**
- Max file size: 5MB per file
- Max request size: 10MB
- Allowed extensions: `jpg`, `jpeg`, `png`, `gif`, `webp`

**cURL:**

```bash
curl -X POST http://localhost:8080/v1/menu-item/photo/upload \
  -H "Authorization: Bearer <token>" \
  -F "files=@/path/to/photo1.jpg" \
  -F "files=@/path/to/photo2.png" \
  -F "menu_item_id=1" \
  -F "description=Kung Pao Chicken presentation"
```

**Response (200):**

```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "file_name": "abc123-photo1.jpg",
      "file_url": "/uploads/abc123-photo1.jpg",
      "menu_item_id": 1,
      "description": "Kung Pao Chicken presentation"
    }
  ]
}
```

### Update Photo Metadata

`PUT /v1/menu-item/photo/{id}`

### Get Photo by ID

`GET /v1/menu-item/photo/{id}`

### List All Photos

`GET /v1/menu-item/photo`

### Delete Photo

`DELETE /v1/menu-item/photo/{id}`

---

## Orders API

### Place Order

`POST /v1/orders`

Creates a new order with items and payment details.

**Request Body:**

```json
{
  "order_date": "2024-01-15",
  "delivery_time": "30-45 min",
  "total_amount": 25.98,
  "tax": 2.60,
  "delivery_fee": 3.00,
  "order_status": "PENDING",
  "user_id": 1,
  "user_phone_number": "+855123456789",
  "restaurant_id": 1,
  "order_items": [
    {
      "menu_item_id": 1,
      "quantity": 2,
      "unit_price": 12.99
    }
  ],
  "payment": {
    "amount": 31.58,
    "payment_method": "KHQR",
    "payment_status": "PENDING"
  }
}
```

**Response (200):**

```json
{
  "status": "success",
  "data": {
    "id": 1,
    "order_id": "ORD-20240115-001",
    "order_date": "2024-01-15T10:30:00Z",
    "total_amount": 25.98,
    "delivery_fee": 3.00,
    "tax": 2.60,
    "order_status": "PENDING",
    "user_id": 1,
    "restaurant_id": 1,
    "payment_id": 1
  }
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/v1/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "order_date": "2024-01-15",
    "total_amount": 25.98,
    "tax": 2.60,
    "delivery_fee": 3.00,
    "order_status": "PENDING",
    "user_id": 1,
    "restaurant_id": 1,
    "order_items": [
      {
        "menu_item_id": 1,
        "quantity": 2,
        "unit_price": 12.99
      }
    ],
    "payment": {
      "amount": 31.58,
      "payment_method": "KHQR",
      "payment_status": "PENDING"
    }
  }'
```

### Update Order Status

`PUT /v1/orders`

Updates the status of an existing order.

**Request Body:**

```json
{
  "order_id": "ORD-20240115-001",
  "order_status": "PREPARING",
  "delivery_partner_id": 5
}
```

**Order Status Flow:**

```
PENDING → PREPARING → OUT_FOR_DELIVERY → DELIVERED
```

**cURL:**

```bash
curl -X PUT http://localhost:8080/v1/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "order_id": "ORD-20240115-001",
    "order_status": "PREPARING"
  }'
```

### Order Statuses

| Status | Description |
|--------|-------------|
| `PENDING` | Order placed, awaiting restaurant confirmation |
| `PREPARING` | Restaurant is preparing the food |
| `OUT_FOR_DELIVERY` | Delivery partner picked up the order |
| `DELIVERED` | Order delivered to customer |

### Payment Methods

| Method | Description |
|--------|-------------|
| `CASH` | Cash on delivery |
| `KHQR` | KH QR code payment |

### Payment Statuses

| Status | Description |
|--------|-------------|
| `PENDING` | Payment initiated |
| `SUCCESS` | Payment confirmed |
| `FAILED` | Payment declined/failed |

---

## Delivery Partners API

### Register Delivery Partner

`POST /v1/delivery-partner`

**Request Body:**

```json
{
  "name": "Sokha Chheng",
  "phone_number": "+855777888999",
  "vehicle_type": "MOTORCYCLE",
  "license_plate": "PP-1234",
  "available": true,
  "current_location": "12.5657,104.9910"
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/v1/delivery-partner \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Sokha Chheng",
    "phone_number": "+855777888999",
    "vehicle_type": "MOTORCYCLE",
    "license_plate": "PP-1234",
    "available": true
  }'
```

### Update Delivery Partner

`PUT /v1/delivery-partner/{id}`

### Get Delivery Partner by ID

`GET /v1/delivery-partner/{id}`

### List All Delivery Partners

`GET /v1/delivery-partner`

### Delete Delivery Partner

`DELETE /v1/delivery-partner/{id}`

### Vehicle Types

| Type | Description |
|------|-------------|
| `MOTORCYCLE` | Motorbike delivery |
| `BICYCLE` | Bicycle delivery |
| `CAR` | Car delivery |

### Delivery Statuses

| Status | Description |
|--------|-------------|
| `PENDING` | Awaiting pickup |
| `PICKED_UP` | Food collected from restaurant |
| `DELIVERED` | Delivery completed |
| `FAILED` | Delivery failed |

---

## AI Chatbot API

### Send Chat Message

`POST /v1/ai/chat`

Send a message to the AI chatbot. The AI uses the knowledge base for context-aware responses about the food delivery platform.

**Request Body:**

```json
{
  "message": "What payment methods do you accept?",
  "conversation_id": "conv_abc123",
  "include_context": true
}
```

**Request Fields:**

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| `message` | String | Yes | Max 2000 chars | User's question |
| `conversation_id` | String | No | - | ID for conversation continuity |
| `include_context` | Boolean | No | Default: `true` | Include knowledge base context |

**Response (200):**

```json
{
  "status": "success",
  "data": {
    "answer": "We accept two payment methods: Cash on Delivery (COD) and KH QR code payments. You can select your preferred method during checkout.",
    "conversation_id": "conv_abc123",
    "source_references": [
      "knowledge/payment-methods.okf",
      "knowledge/order-process.okf"
    ],
    "confidence": 0.96,
    "response_time_ms": 287,
    "timestamp": "2024-01-15T10:30:00Z"
  }
}
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `answer` | String | AI-generated response |
| `conversation_id` | String | Conversation tracking ID |
| `source_references` | String[] | Knowledge sources used |
| `confidence` | Double | Answer confidence (0.0-1.0) |
| `response_time_ms` | Long | Processing time in ms |
| `timestamp` | ISO-8601 | Response generation time |

**cURL:**

```bash
curl -X POST http://localhost:8080/v1/ai/chat \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "message": "What are your delivery hours?",
    "conversation_id": "conv_new_session",
    "include_context": true
  }'
```

### AI Service Health

`GET /v1/ai/health`

Check the operational status of the AI service.

**Response (200):**

```json
{
  "status": "operational",
  "model": "gemini-2.0-flash",
  "capabilities": [
    "chat",
    "knowledge-query",
    "api-help"
  ]
}
```

**cURL:**

```bash
curl http://localhost:8080/v1/ai/health \
  -H "Authorization: Bearer <token>"
```

### AI Capabilities

| Capability | Description |
|------------|-------------|
| `chat` | General conversational AI |
| `knowledge-query` | Answers grounded in OKF knowledge |
| `api-help` | Guidance on using the API |

### Example Conversations

**Asking about delivery:**

```bash
curl -X POST http://localhost:8080/v1/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "How long does delivery usually take?"}'
```

**Asking about the platform:**

```bash
curl -X POST http://localhost:8080/v1/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "How do I track my order?"}'
```

**Getting API help:**

```bash
curl -X POST http://localhost:8080/v1/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "How do I create a new restaurant via the API?"}'
```

---

## Health & Monitoring API

### Composite Health Check

`GET /actuator/health`

Returns the aggregate health of all system components.

**Response (200):**

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "aiService": {
      "status": "UP",
      "details": {
        "service": "Gemini AI",
        "responseTimeMs": 145,
        "apiKeyConfigured": true
      }
    },
    "knowledgeBase": {
      "status": "UP",
      "details": {
        "filesLoaded": 4,
        "lastRefresh": "2024-01-15T08:00:00Z"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 387542016000
      }
    }
  }
}
```

**cURL:**

```bash
curl http://localhost:8080/actuator/health
```

### Application Info

`GET /actuator/info`

**cURL:**

```bash
curl http://localhost:8080/actuator/info
```

### Prometheus Metrics

`GET /actuator/prometheus`

Exports all metrics in Prometheus text format for scraping.

**cURL:**

```bash
curl http://localhost:8080/actuator/prometheus
```

### Key Metrics

`GET /actuator/metrics/{metric-name}`

**Available Metrics:**

| Metric | Description |
|--------|-------------|
| `http.server.requests` | HTTP request statistics |
| `api.ai.chat` | AI chat response times |
| `jvm.memory.used` | JVM memory usage |
| `hikaricp.connections.active` | DB connection pool |
| `cache.gets` | Cache hit/miss stats |
| `system.cpu.usage` | CPU utilization |

**cURL:**

```bash
curl http://localhost:8080/actuator/metrics/http.server.requests
```

---

## Swagger / OpenAPI

Interactive documentation is auto-generated and available at:

| Resource | URL |
|----------|-----|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI Spec (JSON) | `http://localhost:8080/api-docs` |

The Swagger UI provides:
- Interactive endpoint testing
- Request/response schema exploration
- Authentication token input
- Method and tag sorting

---

## SDK / Client Examples

### Java (RestTemplate)

```java
RestTemplate rest = new RestTemplate();
HttpHeaders headers = new HttpHeaders();
headers.setBearerAuth(token);
headers.setContentType(MediaType.APPLICATION_JSON);

UserRequest user = new UserRequest();
user.setUsername("sdk_user");
user.setFirstName("SDK");
user.setLastName("User");

HttpEntity<UserRequest> entity = new HttpEntity<>(user, headers);
ResponseEntity<String> response = rest.postForEntity(
    "http://localhost:8080/v1/users", entity, String.class
);
```

### Python (requests)

```python
import requests

headers = {
    "Authorization": f"Bearer {token}",
    "Content-Type": "application/json"
}

response = requests.post(
    "http://localhost:8080/v1/orders",
    headers=headers,
    json={
        "user_id": 1,
        "restaurant_id": 1,
        "total_amount": 25.98,
        "order_status": "PENDING",
        "order_items": [
            {"menu_item_id": 1, "quantity": 2, "unit_price": 12.99}
        ],
        "payment": {
            "amount": 25.98,
            "payment_method": "CASH",
            "payment_status": "PENDING"
        }
    }
)
```

### JavaScript (fetch)

```javascript
const response = await fetch('http://localhost:8080/v1/ai/chat', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    message: 'What restaurants are open now?',
    include_context: true
  })
});

const data = await response.json();
console.log(data.data.answer);
```

---

## Changelog

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2024-01 | Initial release with core CRUD APIs |
| 1.1.0 | 2024-02 | Added AI chatbot integration |
| 1.2.0 | 2024-03 | Knowledge base (OKF) support |
| 1.3.0 | 2024-04 | Monitoring, health checks, CI/CD pipeline |
