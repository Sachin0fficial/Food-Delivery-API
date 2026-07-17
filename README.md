# Food Delivery API - AI-Powered Platform

> An enterprise-grade, AI-enhanced food delivery backend API built with Java 21, Spring Boot 3.3, and Google Gemini AI integration. Features a knowledge-driven chatbot, Open Knowledge Format (OKF) knowledge base, and production-ready CI/CD pipeline.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green?style=flat-square)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square)
![AI](https://img.shields.io/badge/AI-Gemini%202.0-purple?style=flat-square)
![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-black?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [AI Capabilities](#ai-capabilities)
- [Knowledge Base (OKF)](#knowledge-base-okf)
- [Monitoring & Observability](#monitoring--observability)
- [CI/CD Pipeline](#cicd-pipeline)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

---

## Architecture Overview

```mermaid
flowchart TB
    subgraph Clients["Client Layer"]
        WEB[Web App]
        MOB[Mobile App]
        EXT[External Services]
    end

    subgraph Gateway["API Gateway"]
        SEC[Security Filter]
        LOG[Request Logging]
        RATE[Rate Limiting]
    end

    subgraph Controllers["Controller Layer"]
        UC[User Controller]
        RC[Restaurant Controller]
        OC[Order Controller]
        DC[Delivery Controller]
        MC[Menu Controller]
        AIC[AI Chatbot Controller]
    end

    subgraph Services["Service Layer"]
        US[User Service]
        RS[Restaurant Service]
        OS[Order Service]
        DS[Delivery Service]
        MS[Menu Service]
        PS[Payment Service]
    end

    subgraph AI["AI & Knowledge Layer"]
        GEM[Gemini Service]
        KB[Knowledge Base]
        OKF[OKF Parser]
    end

    subgraph Data["Data Layer"]
        JPA[Spring Data JPA]
        CACHE[Caffeine Cache]
        PG[(PostgreSQL 16)]
    end

    subgraph Monitoring["Observability"]
        ACT[Actuator]
        PROM[Prometheus]
        HEALTH[Health Checks]
    end

    Clients --> Gateway
    Gateway --> Controllers
    Controllers --> Services
    Services --> Data
    AIC --> AI
    AI --> Data
    Services --> Monitoring
    Data --> PG
```

---

## Key Features

### Core Business Features

| Feature | Description |
|---------|-------------|
| **Order Management** | Complete order lifecycle with real-time status tracking (PENDING -> PREPARING -> OUT_FOR_DELIVERY -> DELIVERED) |
| **Restaurant Management** | CRUD operations with menu, photo management, hours, and category support |
| **Delivery Partner System** | Partner registration, availability tracking, and delivery assignment |
| **Payment Processing** | Multi-method payments (Cash, KhQR) with status workflow |
| **User Management** | Profile management, device tracking, and multi-role support |
| **Menu Item Photos** | Multipart file upload with extension validation and size limits |

### AI & Intelligence Features

| Feature | Description |
|---------|-------------|
| **AI Chatbot** | Gemini 2.0 Flash powered conversational AI for customer support |
| **Knowledge Base** | Open Knowledge Format (OKF) structured data for contextual answers |
| **Intelligent Search** | Context-aware query resolution with confidence scoring |
| **Source References** | Transparent AI with traceable answer provenance |

### Production Features

| Feature | Description |
|---------|-------------|
| **Metrics & Monitoring** | Micrometer + Prometheus integration with custom business metrics |
| **Health Checks** | Database, AI service, and knowledge base health monitoring |
| **Structured Logging** | JSON logging with request correlation and timing |
| **Security** | Spring Security with JWT authentication and input validation |
| **CI/CD** | Automated testing, security scanning, and Docker deployment |
| **Caching** | Caffeine in-memory cache for performance optimization |

---

## Tech Stack

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Java (Temurin) | 21 |
| **Framework** | Spring Boot | 3.3.5 |
| **Database** | PostgreSQL | 16 |
| **ORM** | Hibernate / Spring Data JPA | 6.x |
| **AI** | Google Gemini | 2.0 Flash |
| **Cache** | Caffeine | Latest |
| **Security** | Spring Security + JWT (jjwt) | 0.12.6 |
| **API Docs** | SpringDoc OpenAPI | 2.6.0 |
| **Metrics** | Micrometer + Prometheus | Latest |
| **Build** | Maven | 3.9+ |
| **CI/CD** | GitHub Actions | v4 |
| **Container** | Docker + GHCR | Latest |
| **Reactive** | Spring WebFlux (WebClient) | 6.x |
| **Mapping** | ModelMapper | 3.2.1 |

---

## Getting Started

### Prerequisites

- Java 21+ (Temurin recommended)
- Maven 3.9+
- PostgreSQL 16+
- Google Gemini API Key

### Quick Start with Docker Compose

```yaml
# docker-compose.yml
services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: food_delivery
      POSTGRES_USER: food_delivery_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  app:
    build: ./food-delivery-api-main
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: dev
      DB_URL: jdbc:postgresql://postgres:5432/food_delivery
      DB_USERNAME: food_delivery_user
      DB_PASSWORD: ${DB_PASSWORD}
      GEMINI_API_KEY: ${GEMINI_API_KEY}
    depends_on:
      postgres:
        condition: service_healthy

volumes:
  pgdata:
```

```bash
docker compose up -d
```

### Manual Setup

```bash
# Clone the repository
git clone https://github.com/your-org/Food-Delivery-API.git
cd Food-Delivery-API/food-delivery-api-main

# Configure environment
export DB_URL=jdbc:postgresql://localhost:5432/food_delivery
export DB_USERNAME=food_delivery_user
export DB_PASSWORD=your_secure_password
export GEMINI_API_KEY=your_gemini_api_key

# Build and run
mvn clean package -DskipTests
java -jar target/food-delivery-api-0.0.1-SNAPSHOT.jar
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `dev` |
| `SERVER_PORT` | Application port | `8080` |
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/food_delivery` |
| `DB_USERNAME` | Database username | `food_delivery_user` |
| `DB_PASSWORD` | Database password | *Required* |
| `GEMINI_API_KEY` | Google Gemini API key | *Required* |
| `UPLOAD_DIR` | File upload directory | `./uploads` |

---

## API Documentation

Interactive API documentation is available at runtime:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

### Endpoints Overview

| Domain | Method | Endpoint | Description |
|--------|--------|----------|-------------|
| **Users** | POST | `/v1/users` | Create user |
| | PUT | `/v1/users/{id}` | Update user |
| | GET | `/v1/users/{id}` | Get user by ID |
| | GET | `/v1/users` | List all users |
| | DELETE | `/v1/users/{id}` | Delete user |
| **Restaurants** | POST | `/v1/restaurants` | Create restaurant |
| | PUT | `/v1/restaurants/{id}` | Update restaurant |
| | GET | `/v1/restaurants/{id}` | Get restaurant by ID |
| | GET | `/v1/restaurants` | List all restaurants |
| | DELETE | `/v1/restaurants/{id}` | Delete restaurant |
| **Orders** | POST | `/v1/orders` | Place order |
| | PUT | `/v1/orders` | Update order status |
| **Menu Items** | POST | `/v1/menu-item` | Create menu item |
| | PUT | `/v1/menu-item/{id}` | Update menu item |
| | GET | `/v1/menu-item/{id}` | Get menu item |
| | GET | `/v1/menu-item` | List all menu items |
| | DELETE | `/v1/menu-item/{id}` | Delete menu item |
| **Photos** | POST | `/v1/menu-item/photo/upload` | Upload photos |
| | PUT | `/v1/menu-item/photo/{id}` | Update photo metadata |
| | GET | `/v1/menu-item/photo/{id}` | Get photo by ID |
| | GET | `/v1/menu-item/photo` | List all photos |
| | DELETE | `/v1/menu-item/photo/{id}` | Delete photo |
| **Delivery** | POST | `/v1/delivery-partner` | Register partner |
| | PUT | `/v1/delivery-partner/{id}` | Update partner |
| | GET | `/v1/delivery-partner/{id}` | Get partner |
| | GET | `/v1/delivery-partner` | List partners |
| | DELETE | `/v1/delivery-partner/{id}` | Remove partner |
| **AI Chat** | POST | `/v1/ai/chat` | AI conversation |
| | GET | `/v1/ai/health` | AI service health |

> For detailed request/response schemas, see [API_DOCUMENTATION.md](./food-delivery-api-main/API_DOCUMENTATION.md)

---

## AI Capabilities

### Chatbot Architecture

```mermaid
sequenceDiagram
    participant C as Client
    participant API as ChatbotController
    participant GS as GeminiService
    participant KB as Knowledge Base
    participant GEM as Google Gemini API

    C->>API: POST /v1/ai/chat
    API->>GS: chat(ChatRequest)
    GS->>KB: Load context (OKF)
    KB-->>GS: Structured knowledge
    GS->>GEM: Generate response with context
    GEM-->>GS: AI response
    GS-->>API: ChatResponse (answer + confidence + sources)
    API-->>C: 200 OK with response
```

### Chat Request/Response

**Request:**
```json
{
  "message": "What are the delivery hours?",
  "conversation_id": "conv_abc123",
  "include_context": true
}
```

**Response:**
```json
{
  "answer": "Our delivery partners operate from 8 AM to 11 PM daily...",
  "conversation_id": "conv_abc123",
  "source_references": ["knowledge/delivery-policy.okf"],
  "confidence": 0.94,
  "response_time_ms": 342,
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Capabilities

- **Chat** - Natural language conversation about food delivery topics
- **Knowledge Query** - Answers grounded in structured OKF knowledge base
- **API Help** - Guidance on using the food delivery platform

---

## Knowledge Base (OKF)

The Open Knowledge Format (OKF) provides structured knowledge that the AI uses for contextual, accurate responses.

```mermaid
flowchart LR
    subgraph Sources["Knowledge Sources"]
        FAQ[FAQs]
        POL[Policies]
        MENU[Menu Data]
        OPS[Operations]
    end

    subgraph Processing["Processing"]
        PARSE[OKF Parser]
        INDEX[Indexer]
        VALID[Validator]
    end

    subgraph Storage["Storage"]
        STORE[Knowledge Store]
        CACHE2[Cache Layer]
    end

    subgraph Consumption["AI Consumption"]
        CTX[Context Builder]
        GEN[Response Generator]
    end

    Sources --> Processing
    Processing --> Storage
    Storage --> Consumption
```

### OKF Structure

Knowledge is organized into domains with metadata, making it easy for the AI to find relevant context:

```
knowledge/
  delivery-policy.okf     # Delivery rules and hours
  payment-methods.okf     # Supported payment information
  restaurant-faq.okf      # Common restaurant questions
  order-process.okf       # Order lifecycle documentation
```

---

## Monitoring & Observability

### Health Endpoints

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Composite health (DB + AI + Knowledge Base) |
| `/actuator/info` | Application info and version |
| `/actuator/metrics` | All application metrics |
| `/actuator/prometheus` | Prometheus-compatible metrics export |

### Custom Health Indicators

```mermaid
flowchart TD
    HEALTH["/actuator/health"]
    HEALTH --> DB_H["Database Health"]
    HEALTH --> AI_H["AI Service Health"]
    HEALTH --> KB_H["Knowledge Base Health"]

    DB_H --> |"Check connection"| PG2[(PostgreSQL)]
    AI_H --> |"Verify API key + ping"| GEM2[Gemini API]
    KB_H --> |"Validate files"| FS[File System]

    style HEALTH fill:#4CAF50,color:white
    style DB_H fill:#2196F3,color:white
    style AI_H fill:#9C27B0,color:white
    style KB_H fill:#FF9800,color:white
```

### Business Metrics

| Metric | Type | Description |
|--------|------|-------------|
| `api.ai.chat` | Timer | AI chat endpoint response time |
| `http.server.requests` | Timer | All HTTP request durations |
| `jvm.memory.used` | Gauge | JVM memory utilization |
| `hikaricp.connections.active` | Gauge | Active database connections |
| `cache.gets` | Counter | Cache hit/miss ratio |

### Structured Logging

Requests are logged with correlation and timing via the `RequestLoggingFilter`:

```
2024-01-15 10:30:00.123 [http-nio-8080-exec-1] INFO  c.c.filter.RequestLoggingFilter - POST /v1/orders 200 45ms
```

---

## CI/CD Pipeline

### Continuous Integration

```mermaid
flowchart LR
    subgraph Trigger["Trigger"]
        PUSH[Push to main/develop]
        PR[Pull Request]
    end

    subgraph Build["Build Job"]
        CHECKOUT[Checkout]
        JDK[Setup JDK 21]
        COMPILE[Maven Compile]
        UNIT[Unit Tests]
        INT[Integration Tests]
        PKG[Package JAR]
    end

    subgraph Quality["Code Quality"]
        SPOT[SpotBugs]
        CHECK[Checkstyle]
    end

    subgraph Security["Security Scan"]
        OWASP[OWASP Dependency Check]
        REPORT[Upload Report]
    end

    subgraph Deploy["Docker Deploy"]
        BUILDX[Docker Buildx]
        GHCR[Push to GHCR]
    end

    Trigger --> Build
    Build --> Quality
    Build --> Security
    Build & Security --> Deploy
```

### Release Pipeline

```mermaid
flowchart TD
    TAG["Git Tag (v*)"] --> VER[Extract Version]
    VER --> SET[Set Maven Version]
    SET --> JAR[Build Release JAR]
    JAR --> LOG[Generate Changelog]
    LOG --> GHR[Create GitHub Release]
    GHR --> DOCK[Build Docker Image]
    DOCK --> PUB[Push to GHCR with version tag]
```

### Pipeline Features

- **PostgreSQL 16** service container for integration tests
- **Maven caching** for faster builds
- **Test reporting** via dorny/test-reporter
- **SpotBugs + Checkstyle** code quality gates
- **OWASP Dependency Check** for vulnerability scanning
- **Multi-tag Docker builds** (latest + SHA + version)
- **Automated GitHub Releases** with changelogs

---

## Order Lifecycle

```mermaid
stateDiagram-v2
    [*] --> PENDING: Order placed
    PENDING --> PREPARING: Restaurant accepts
    PREPARING --> OUT_FOR_DELIVERY: Partner picks up
    OUT_FOR_DELIVERY --> DELIVERED: Delivery confirmed
    DELIVERED --> [*]

    PENDING --> CANCELLED: User cancels
    PREPARING --> CANCELLED: Restaurant declines
    CANCELLED --> [*]
```

### Payment State Machine

```mermaid
stateDiagram-v2
    [*] --> PENDING: Payment initiated
    PENDING --> SUCCESS: Payment confirmed
    PENDING --> FAILED: Payment declined
    FAILED --> PENDING: Retry payment
    SUCCESS --> [*]
```

---

## Database Schema

```mermaid
erDiagram
    USER ||--o{ DEVICE : has
    USER ||--o{ ORDER : places
    RESTAURANT ||--o{ MENU_ITEM : offers
    MENU_ITEM ||--o{ MENU_ITEM_PHOTO : has
    ORDER ||--o{ ORDER_ITEM : contains
    ORDER ||--|| PAYMENT : has
    ORDER }o--|| RESTAURANT : from
    ORDER }o--o| DELIVERY_PARTNER : assigned_to
    DELIVERY_PARTNER ||--o{ DELIVERY : makes

    USER {
        bigint id PK
        string username UK
        string first_name
        string last_name
        string gender
        date date_of_birth
        string email
        string phone_number
        string address
        enum user_type
        string status
    }

    RESTAURANT {
        bigint id PK
        string code
        string name
        string category
        string description
        float rating
        string address
        string phone_number
        string logo_url
        time open_time
        time close_time
    }

    ORDER {
        bigint id PK
        string order_id
        timestamp order_date
        float total_amount
        float delivery_fee
        float tax
        enum order_status
        bigint user_id FK
        bigint restaurant_id FK
        bigint delivery_partner_id FK
        bigint payment_id FK
    }

    MENU_ITEM {
        bigint id PK
        string name
        string description
        float price
        string category
        bigint restaurant_id FK
    }

    DELIVERY_PARTNER {
        bigint id PK
        string name
        string phone_number
        enum vehicle_type
        boolean available
    }

    PAYMENT {
        bigint id PK
        float amount
        enum payment_method
        enum payment_status
        bigint order_id FK
    }
```

---

## Project Structure

```
Food-Delivery-API/
├── .github/
│   └── workflows/
│       ├── ci.yml                    # CI pipeline (build, test, scan, docker)
│       └── release.yml               # Release pipeline (tag-based)
├── food-delivery-api-main/
│   ├── src/main/java/com/codestorykh/
│   │   ├── FoodDeliveryApiApplication.java
│   │   ├── ai/
│   │   │   ├── controller/           # AI chatbot endpoints
│   │   │   ├── model/                # Chat request/response DTOs
│   │   │   └── service/              # Gemini AI integration
│   │   ├── config/
│   │   │   ├── CacheConfig.java      # Caffeine cache configuration
│   │   │   ├── MetricsConfig.java    # Micrometer metrics setup
│   │   │   ├── WebClientConfig.java  # Reactive HTTP client
│   │   │   └── WebMvcConfig.java     # MVC configuration
│   │   ├── constant/                 # Application constants
│   │   ├── controller/               # REST API controllers
│   │   ├── dto/                      # Request/Response DTOs
│   │   ├── enumeration/              # Business enums (OrderStatus, PaymentMethod, etc.)
│   │   ├── exception/                # Global exception handling
│   │   ├── filter/                   # HTTP filters (logging, security)
│   │   ├── health/                   # Custom health indicators
│   │   ├── model/                    # JPA entities
│   │   ├── repository/               # Spring Data repositories
│   │   ├── service/                  # Business logic layer
│   │   │   ├── handler/              # Strategy pattern handlers
│   │   │   └── impl/                 # Service implementations
│   │   └── utils/                    # Utility classes
│   ├── src/main/resources/
│   │   ├── application.yml           # Main configuration
│   │   ├── application-dev.yml       # Development profile
│   │   └── logback-spring.xml        # Logging configuration
│   └── pom.xml                       # Maven dependencies
├── API_DOCUMENTATION.md              # Detailed API docs
└── README.md                         # This file
```

---

## Contributing

### Development Workflow

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/your-feature`
3. **Commit** with conventional commits: `feat:`, `fix:`, `docs:`, `refactor:`
4. **Test** locally: `mvn clean verify`
5. **Push** and create a Pull Request

### Code Standards

- Follow Java 21 coding conventions
- Use Lombok for boilerplate reduction
- Write unit tests for all service methods
- Include integration tests for controllers
- Keep DTOs separate from entities

### Branch Strategy

| Branch | Purpose |
|--------|---------|
| `main` | Production-ready code |
| `develop` | Integration branch |
| `feature/*` | New features |
| `fix/*` | Bug fixes |
| `release/*` | Release preparation |

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

<p align="center">
  Built with Java 21 and Spring Boot 3.3.5<br>
  AI powered by Google Gemini 2.0 Flash
</p>
