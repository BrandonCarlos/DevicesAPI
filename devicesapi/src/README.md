# 📱 Devices API

A modern, production-ready REST API built with **Spring Boot** and **Java 21**, using solid architectural principles and a full development stack.  
This API manages **devices**, providing full CRUD operations, validations, caching, observability, and is fully containerized.

---

## 🛠️ Tech Stack

| Technology         | Purpose                                       |
|--------------------|-----------------------------------------------|
| **Java 21**        | Latest LTS Java version                       |
| **Spring Boot**    | Backend framework                             |
| **PostgreSQL**     | Relational database for persistent storage    |
| **Redis**          | In-memory caching                             |
| **Docker & Docker Compose** | Containerization & orchestration     |
| **JUnit & Mockito**| Unit testing and mocking                      |
| **Bean Validation (Jakarta)** | Input validation                  |
| **Swagger / OpenAPI** | API documentation                        |
| **Prometheus**     | Metrics collection                            |
| **Grafana**        | Metrics dashboards                            |

---

## 🧠 Architectures Used

The application is structured using the following principles:

### ✅ Domain-Driven Design (DDD)
- Emphasizes domain modeling and business logic at the core.
- Keeps your business rules independent from frameworks and technologies.

### ✅ Hexagonal Architecture (Ports & Adapters)
- Separates the core logic from external systems (DB, APIs, web layer).
- Makes the app highly testable and easy to adapt/replace technologies.

### ✅ Clean Architecture
- Layers: **Domain** → **Application** → **Adapters** → **Infrastructure**
- High maintainability, testability, and scalability.

---

## 🗄️ PostgreSQL – Primary Database

The API uses **PostgreSQL** for data persistence.

- Dockerized via `docker-compose`
- Manages device entities using Spring Data JPA
- Connection pooling via **HikariCP**

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://db:5432/devices
    username: devices_user
    password: secret
Parameter	Value
Host	localhost or db (Docker)
Port	5432
Username	put your username here
Password	put your password here
Database	devices

⚡ Redis – Caching Layer
Caching is implemented using Redis, integrated with Spring’s @Cacheable and @CacheEvict annotations.

Speeds up repeated reads (e.g., fetching devices)

Reduces load on PostgreSQL

yaml
Copy code
spring:
  cache:
    type: redis
  redis:
    host: redis
    port: 6379
✅ Validations
All incoming requests are validated using Jakarta Bean Validation:

java
Copy code
public class CreateDeviceRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotNull
    private String status;
}
Invalid requests return descriptive error responses (400 Bad Request)

Reduces logic duplication and ensures clean data entry

🔍 API Documentation (Swagger)
Interactive Swagger UI is available at:

bash
Copy code
http://localhost:8080/swagger-ui.html
You can:

Try out endpoints live

See example requests/responses

Explore available resources and schemas

📊 Monitoring – Prometheus & Grafana
Your application is metrics-enabled out of the box.

Spring Boot exposes metrics via /actuator/prometheus

Prometheus scrapes metrics every few seconds

Grafana provides real-time dashboards and system insights

Tool	URL
Prometheus	http://localhost:9090
Grafana	http://localhost:3000

In Grafana, import a Spring Boot dashboard (e.g., ID 4701) and configure Prometheus as the data source (http://prometheus:9090).

📦 Running the Application
⚙️ Prerequisites
Java 21

Docker & Docker Compose

Maven (or ./mvnw)

🐳 Launch with Docker
Run the entire stack with:

bash
Copy code
docker-compose up --build
This will start:

Spring Boot API

PostgreSQL

Redis

Prometheus

Grafana

📬 API Overview
➕ Create a Device
http
Copy code
POST /devices
Content-Type: application/json

{
  "name": "Smart Sensor",
  "type": "IoT",
  "status": "in-use"
}
📤 Response
json
Copy code
{
  "id": "uuid",
  "name": "Smart Sensor",
  "type": "IoT",
  "status": "in-use",
  "createdAt": "2025-08-24T15:00:00Z"
}
🔁 Other Endpoints
GET /devices

GET /devices/{id}

PUT /devices/{id}

PATCH /devices/{id}

DELETE /devices/{id}

🧪 Running Tests
bash
Copy code
./mvnw test
Unit tests for core business logic and services

Mockito used for mocking dependencies

Test data is isolated (DB and cache are mocked)

📁 Project Structure
bash
Copy code
src/
├── domain/         # Domain models and interfaces
├── application/    # Business use cases
├── adapters/       # REST controllers, input/output ports
├── infrastructure/ # Repositories, Redis, etc.
└── config/         # Spring configuration
🧰 Common Commands
Task	Command
Build project	./mvnw clean install
Run locally	./mvnw spring-boot:run
Run with Docker	docker-compose up --build
Run tests	./mvnw test