<<<<<<< HEAD
# Real-Time Chat Application - Microservices Architecture

A production-ready real-time chat application built with Java Spring Boot, Spring Cloud, WebSocket (STOMP), Apache Kafka, and MySQL.

## Architecture Overview

The application follows a microservices architecture with the following services:

1. **Eureka Server** (Port 8761) - Service Discovery
2. **API Gateway** (Port 8082) - Spring Cloud Gateway with JWT validation
3. **Auth Service** (Port 8081) - User authentication, registration, JWT token generation
4. **User Service** (Port 8084) - User profile management
5. **Chat Service** (Port 8083) - WebSocket + STOMP for real-time messaging
6. **Message Service** (Port 8085) - Kafka consumer for message persistence

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Cloud 2023.0.0**
- **Spring Cloud Gateway** - API Gateway
- **Spring Cloud Netflix Eureka** - Service Discovery
- **Spring WebSocket + STOMP** - Real-time messaging
- **Apache Kafka** - Message queue
- **MySQL** - Database
- **Spring Data JPA** - ORM
- **JWT** - Authentication
- **Maven** - Build tool

## Prerequisites

Before running the application, ensure you have:

1. **Java 17** installed
2. **Maven** installed
3. **MySQL** server running (default port 3306)
4. **Apache Kafka** running (default port 9092)
5. **Zookeeper** (required for Kafka)

## Database Setup

1. Run the SQL script to create databases and tables:
```bash
mysql -u root -p < database-schema.sql
```

The script creates three databases:
- `auth_service` - For authentication
- `user_service` - For user profiles
- `chat_service` - For chat messages

## Configuration

### Database Configuration
Update database credentials in each service's `application.yml` or `application.properties`:
- Default username: `root`
- Default password: `Sohan@123#`
- Update as needed for your environment

### Kafka Configuration
Ensure Kafka is running on `localhost:9092`. Update if different.

### JWT Secret
All services use the same JWT secret: `MySuperSecretKeyForJWTTokenGeneration123456789`
Update in production for security.

## Running the Services

### 1. Start Eureka Server
```bash
cd EurekaServer
mvn spring-boot:run
```
Access Eureka Dashboard: http://localhost:8761

### 2. Start Auth Service
```bash
cd AuthService
mvn spring-boot:run
```

### 3. Start User Service
```bash
cd ProfieService  # Note: Service name has typo but functional
mvn spring-boot:run
```

### 4. Start API Gateway
```bash
cd GatewayService
mvn spring-boot:run
```

### 5. Start Chat Service
```bash
cd ChatService
mvn spring-boot:run
```

### 6. Start Message Service
```bash
cd MessageService
mvn spring-boot:run
```

## API Endpoints

### Gateway Routes (Port 8082)
All API calls go through the gateway:

- `POST /api/auth/signup` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh access token
- `GET /api/users` - Get all users (Protected)
- `GET /api/users/{id}` - Get user by ID (Protected)
- `GET /api/messages/between/{userId1}/{userId2}` - Get messages between users (Protected)

### WebSocket Endpoint
- `ws://localhost:8083/ws` - STOMP WebSocket endpoint for real-time chat

## Authentication Flow

1. **Register/Login**: POST to `/api/auth/signup` or `/api/auth/login`
   - Returns `accessToken` and `refreshToken`

2. **Access Protected APIs**: Include JWT in Authorization header:
   ```
   Authorization: Bearer <accessToken>
   ```

3. **WebSocket Connection**: Include token in connection:
   ```
   ws://localhost:8083/ws?token=<accessToken>
   ```
   Or in STOMP headers:
   ```
   Authorization: Bearer <accessToken>
   ```

## Message Flow

1. Client sends message via WebSocket to Chat Service
2. Chat Service publishes message to Kafka topic `chat.messages`
3. Message Service consumes from Kafka and persists to MySQL
4. Message Service publishes delivery notification to `chat.messages.delivery`
5. Chat Service can consume delivery notifications for real-time updates

## Kafka Topics

- `chat.messages` - Messages sent by users (consumed by MessageService)
- `chat.messages.delivery` - Delivery notifications (for future use)

## Project Structure

```
RealTime/
├── EurekaServer/          # Service Discovery
├── GatewayService/        # API Gateway
├── AuthService/           # Authentication Service
├── ProfieService/         # User Service (note typo in folder name)
├── ChatService/           # WebSocket + STOMP Service
├── MessageService/        # Message Persistence Service
├── database-schema.sql    # Database schema
└── README.md              # This file
```

## Development Notes

- All services register with Eureka for service discovery
- Gateway routes requests to appropriate services using service names
- JWT tokens are validated at the gateway level
- WebSocket connections are authenticated using JWT
- Messages are persisted asynchronously via Kafka
- CORS is enabled for frontend integration

## Troubleshooting

1. **Services not registering with Eureka**: Check Eureka server is running and service configuration
2. **JWT validation failing**: Ensure all services use the same JWT secret
3. **Kafka connection issues**: Verify Kafka and Zookeeper are running
4. **Database connection errors**: Check MySQL is running and credentials are correct
5. **WebSocket connection fails**: Verify JWT token is valid and included in connection

## Production Considerations

1. Change default JWT secret
2. Use proper secret management (Vault, AWS Secrets Manager, etc.)
3. Enable HTTPS/TLS
4. Configure proper CORS origins
5. Add rate limiting
6. Implement proper logging and monitoring
7. Use connection pooling for databases
8. Configure Kafka for production (replication, partitions, etc.)
9. Add circuit breakers for resilience
10. Implement proper error handling and retries

## License

This is a demonstration project.
=======
# 💬 Real-Time Chat Application

A **scalable microservices-based real-time chat platform** built using **Spring Boot**, **React**, **WebSockets**, **Kafka**, **Redis**, and **MySQL**.  
Designed for instant messaging, presence tracking, and future-ready extensibility.

---

## 🚀 Overview

This project enables **real-time communication** between users through a modern and modular architecture.  
Each service is independent, ensuring better scalability, maintainability, and fault isolation.

---

## 🧱 Architecture Overview

- **Frontend:** React (modular component-based structure)
- **Backend:** Spring Boot microservices
- **Real-time Communication:** WebSocket + Redis Pub/Sub
- **Database:** MySQL (persistent storage)
- **Cache & Presence:** Redis
- **Message Queue:** Kafka (asynchronous message handling)

---

## ⚙️ Core Backend Services

### 1. **Auth Service**
- Handles user login, signup, and JWT token generation.
- Verifies tokens during WebSocket handshake.

### 2. **User/Profile Service**
- Manages user profiles, friend lists, and profile updates.
- Provides public and private profile APIs.

### 3. **Chat Service**
- Stores and retrieves chat messages.
- Tracks delivery, read receipts, and conversation history.

### 4. **Presence Service**
- Tracks online/offline/typing status.
- Uses Redis for real-time updates and caching.

### 5. **WebSocket Gateway**
- Maintains live connections between client and server.
- Routes messages to appropriate users or groups in real-time.

---

## 🧠 High-Level Features

- 🔄 Real-time messaging (WebSocket-based)
- 🔐 Secure login with JWT
- 💡 Online/offline/typing indicators
- 🧾 Message read and delivery receipts
- ⚡ Fast caching with Redis
- 📡 Kafka-based asynchronous message delivery
- 🧩 Modular microservices structure

---

## 🧭 Future Enhancements

- 📎 File and media sharing  
- 🔔 Push notifications  
- 👥 Group chat and admin controls  
- 🔍 Chat search and message archiving  
- 🧰 Full CI/CD pipeline with GitHub Actions and Docker  

---

## 🔐 Security Highlights

- ✅ JWT-based token authentication  
- 🔒 Secure WebSocket connections  
- 🧩 Role-based access for protected endpoints  
- 🧠 Redis for connection mapping and session handling  

---

## 🧰 Tech Stack

| Layer | Technology |
| ------ | ----------- |
| Frontend | React, Tailwind CSS |
| Backend | Spring Boot (Java 17) |
| Real-Time | WebSocket, Redis Pub/Sub |
| Database | MySQL |
| Messaging | Kafka |
| Caching | Redis |
| Deployment | Docker, Docker Compose |
| Monitoring (Future) | Prometheus, Grafana |

---
>>>>>>> a5f54e4c0ce762bd558ccafb122e931d632f0085

