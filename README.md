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

