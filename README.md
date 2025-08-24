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

📱 Device Management API

This API allows you to manage devices through basic CRUD operations. Below are the available endpoints with request and response examples.

🔹 POST / Create a Device

Endpoint:
POST http://localhost:8080/api/devices

Request Body:

{
  "name": "Device C",
  "brand": "Apple",
  "state": "AVAILABLE"
}


Response: 200 OK

{
  "id": "09551562-255f-48ae-9ee5-c6a2c31335bf",
  "name": "Device C",
  "brand": "Apple",
  "state": "AVAILABLE",
  "creationTime": "2025-08-24T17:13:16.195903"
}

🔹 GET / Get All Devices

Endpoint:
GET http://localhost:8080/api/devices

Response: 200 OK
(Sample Output)

[
  {
    "id": "963fed21-d30c-4a24-8242-71150cd65bc8",
    "name": "iPhone 15 Pro",
    "brand": "Apple",
    "state": "IN_USE",
    "creationTime": "2025-08-21T20:07:22.075232"
  },
  ...
]

🔹 GET / Get Device by ID

Endpoint:
GET http://localhost:8080/api/devices/{id}

Example:
GET http://localhost:8080/api/devices/170e75dd-7db9-46dc-9218-29391ba2be15

Response: 200 OK

{
  "id": "170e75dd-7db9-46dc-9218-29391ba2be15",
  "name": "Device C",
  "brand": "Apple",
  "state": "AVAILABLE",
  "creationTime": "2025-08-24T17:15:11.073007"
}

🔹 GET / Get Devices by Brand

Endpoint:
GET http://localhost:8080/api/devices?brand=Apple

Response: 200 OK
(Sample Output: Same structure as “Get All Devices” with filtered results)

🔹 GET / Get Devices by State

Endpoint:
GET http://localhost:8080/api/devices?state=AVAILABLE

Response: 200 OK
(Sample Output: Same structure as “Get All Devices” with filtered results)

🔹 DELETE / Delete Device by ID

Endpoint:
DELETE http://localhost:8080/api/devices/{id}

Example:
DELETE http://localhost:8080/api/devices/0d194268-9ea9-4642-a261-a7eed54f92ac

Response: 204 No Content

⚠️ Validation:
Devices with state: IN_USE cannot be deleted.

🔹 PATCH / Update Device State

Endpoint:
PATCH http://localhost:8080/api/devices/{id}

Example:
PATCH http://localhost:8080/api/devices/0d194268-9ea9-4642-a261-a7eed54f92ac

Request Body:

{
  "state": "INACTIVE"
}


Response: 200 OK (with updated device object)

⚠️ Validations:

creationTime cannot be updated.

name and brand cannot be updated if the device is IN_USE.

Devices in IN_USE state cannot be deleted.

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

public class CreateDeviceRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotNull
    private DeviceState state;
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
