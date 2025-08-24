# 📱 Devices API

A modern, scalable RESTful API built with **Spring Boot** and **Java 21**, designed following the principles of **Domain-Driven Design (DDD)**, **Hexagonal Architecture**, and **Clean Architecture**.
This service manages **devices** through full CRUD operations, integrates **PostgreSQL** for persistence, uses **Redis** for caching, and comes fully containerized with **Docker**.

---

## 🔧 Technologies Used

| Technology        | Purpose                                     |
|-------------------|---------------------------------------------|
| Java 21           | Modern language features and performance    |
| Spring Boot       | Framework for rapid REST API development    |
| DDD               | Focused business domain logic               |
| Hexagonal Architecture | Clear separation of concerns        |
| Clean Architecture | Maintainability and testability            |
| PostgreSQL        | Relational database for persistent storage  |
| Redis             | In-memory cache for performance             |
| Docker            | Containerization and environment consistency|
| JUnit & Mockito   | Unit testing and mocking                    |
| Bean Validation   | Robust request data validation              |
| Swagger (Springdoc) | Auto-generated API documentation         |

---

## 📦 Features

- ✅ RESTful API for managing **devices**
- ✅ Full CRUD operations:
  - `GET /devices`
  - `GET /devices/{id}`
  - `POST /devices`
  - `PUT /devices/{id}`
  - `PATCH /devices/{id}`
  - `DELETE /devices/{id}`
- ✅ Validation of input data (using Jakarta Bean Validation)
- ✅ Redis-based caching for performance
- ✅ PostgreSQL as the primary data store
- ✅ Swagger/OpenAPI 3 documentation
- ✅ Unit-tested core logic
- ✅ Dockerized for easy deployment

---

## 🧠 Architecture Overview

This project follows **Hexagonal + Clean Architecture** layered around DDD concepts:

src/
├── domain/ # Core domain models and interfaces
├── application/ # Use cases, services, and logic orchestration
├── infrastructure/ # Implementations: DB, Redis, external APIs
├── adapters/ # Controllers (input), persistence, etc.
└── config/ # Spring Boot configuration classes

markdown
Copy code

**Key Principles:**

- **Domain-first design**: Business logic comes first
- **Ports and Adapters (Hexagonal)**: Easy to swap out infrastructure
- **Clean separation** between layers for testing and maintenance

---

## 🚀 Getting Started

### ✅ Prerequisites

- [Java 21](https://jdk.java.net/21/)
- [Docker](https://www.docker.com/)
- [Maven](https://maven.apache.org/) (or use `./mvnw`)

---

## 🐳 Running with Docker

The app comes with a ready-to-go `docker-compose.yml` file.

```bash
docker-compose up --build
This command will start:

Spring Boot app

PostgreSQL (default port: 5432)

Redis (default port: 6379)

Once running, you can access:

API Docs: http://localhost:8080/swagger-ui.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

🔍 API Documentation with Swagger
This project uses Springdoc OpenAPI to generate interactive API docs.

📘 Accessing Swagger UI
txt
Copy code
http://localhost:8080/swagger-ui.html
You can interact with all endpoints directly from the browser, including request/response examples, validation constraints, and models.

🧪 Running Tests
To run unit tests:

bash
Copy code
./mvnw test
Tests are written using JUnit 5 and Mockito, covering application and domain layers. Redis and database dependencies are mocked to ensure fast and isolated tests.

📬 Example API Usage
✅ Create a Device
http
Copy code
POST /devices
Content-Type: application/json

{
  "name": "Smart Thermostat",
  "type": "IoT",
  "status": "AVAILABLE"
}
Response:

json
Copy code
{
  "id": "7c58e2c4-8b5a-4b11-b7b7-f6c1bcf3fbee",
  "name": "Smart Thermostat",
  "type": "IoT",
  "status": "available",
  "createdAt": "2025-08-24T14:52:00Z"
}
🗃 Environment Variables
These values can be configured in your .env or passed at runtime:

Variable	Default	Description
SPRING_DATASOURCE_URL	jdbc:postgresql://db:5432/devices	PostgreSQL URL
SPRING_REDIS_HOST	redis	Redis host
SPRING_REDIS_PORT	6379	Redis port
SERVER_PORT	8080	App port

🧼 Code Quality
✅ Linting and formatting via IDE or plugins

✅ Package-by-feature structure

✅ Fully unit-tested domain and services

✅ Loose coupling, high cohesion

📂 Project Structure Overview
bash
Copy code
src/
├── main/
│   ├── java/
│   │   └── com/yourcompany/devicesapi/
│   │       ├── domain/
│   │       ├── application/
│   │       ├── adapters/
│   │       ├── infrastructure/
│   │       └── config/
│   └── resources/
│       ├── application.yml
│       └── ...
└── test/
    └── java/
        └── com/yourcompany/devicesapi/
🧰 Useful Commands
Build project: ./mvnw clean install

Start local app (non-docker): ./mvnw spring-boot:run

Format code (if using Spotless or Checkstyle): ./mvnw spotless:apply