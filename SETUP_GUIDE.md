# Setup Guide - Real-Time Chat Application

## Quick Start

### 1. Prerequisites Installation

#### Install Java 17
- Download from: https://adoptium.net/
- Verify: `java -version`

#### Install Maven
- Download from: https://maven.apache.org/download.cgi
- Verify: `mvn -version`

#### Install MySQL
- Download from: https://dev.mysql.com/downloads/mysql/
- Default port: 3306
- Create root user with password: `Sohan@123#` (or update in config files)

#### Install Apache Kafka
- Download from: https://kafka.apache.org/downloads
- Extract and start Zookeeper:
  ```bash
  bin/zookeeper-server-start.sh config/zookeeper.properties
  ```
- Start Kafka:
  ```bash
  bin/kafka-server-start.sh config/server.properties
  ```

### 2. Database Setup

Run the SQL script:
```bash
mysql -u root -p < database-schema.sql
```

Or manually execute the SQL in `database-schema.sql` using MySQL client.

### 3. Start Services (in order)

#### Terminal 1: Eureka Server
```bash
cd EurekaServer
mvn clean install
mvn spring-boot:run
```
Wait for: "Started EurekaServerApplication" 
Access: http://localhost:8761

#### Terminal 2: Auth Service
```bash
cd AuthService
mvn clean install
mvn spring-boot:run
```
Wait for: Service registered with Eureka

#### Terminal 3: User Service
```bash
cd ProfieService
mvn clean install
mvn spring-boot:run
```
Wait for: Service registered with Eureka

#### Terminal 4: API Gateway
```bash
cd GatewayService
mvn clean install
mvn spring-boot:run
```
Wait for: Service registered with Eureka

#### Terminal 5: Chat Service
```bash
cd ChatService
mvn clean install
mvn spring-boot:run
```
Wait for: Service registered with Eureka

#### Terminal 6: Message Service
```bash
cd MessageService
mvn clean install
mvn spring-boot:run
```
Wait for: Service registered with Eureka

### 4. Verify Setup

1. **Eureka Dashboard**: http://localhost:8761
   - Should show all services registered

2. **Test Registration**:
   ```bash
   curl -X POST http://localhost:8082/api/auth/signup \
     -H "Content-Type: application/json" \
     -d '{"email":"test@example.com","password":"password123","username":"testuser"}'
   ```

3. **Test Login**:
   ```bash
   curl -X POST http://localhost:8082/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"test@example.com","password":"password123"}'
   ```

4. **Test Protected Endpoint** (use token from login):
   ```bash
   curl -X GET http://localhost:8082/api/users \
     -H "Authorization: Bearer <YOUR_TOKEN>"
   ```

### 5. WebSocket Connection Test

Use a WebSocket client (e.g., Postman, wscat, or browser console):

```javascript
const socket = new SockJS('http://localhost:8083/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({
  'Authorization': 'Bearer <YOUR_TOKEN>'
}, function(frame) {
  console.log('Connected: ' + frame);
  
  // Subscribe to messages
  stompClient.subscribe('/topic/messages', function(message) {
    console.log('Received:', JSON.parse(message.body));
  });
  
  // Send a message
  stompClient.send('/app/message.send', {}, JSON.stringify({
    recipientId: 2,
    content: 'Hello!',
    contentType: 'text'
  }));
});
```

## Troubleshooting

### Services won't start
- Check if ports are available (8761, 8081-8085)
- Verify MySQL is running
- Verify Kafka is running

### Eureka registration fails
- Ensure Eureka Server is running first
- Check network connectivity
- Verify service names match in config files

### Database connection errors
- Verify MySQL is running: `mysql -u root -p`
- Check credentials in `application.yml`
- Ensure databases are created

### Kafka connection errors
- Verify Zookeeper is running (port 2181)
- Verify Kafka is running (port 9092)
- Check Kafka logs for errors

### JWT validation fails
- Ensure all services use the same JWT secret
- Check token expiration
- Verify token format: `Bearer <token>`

### WebSocket connection fails
- Check CORS configuration
- Verify JWT token is valid
- Check token is included in connection headers

## Configuration Changes

### Change Database Password
Update in all services' `application.yml`:
```yaml
spring:
  datasource:
    password: YOUR_PASSWORD
```

### Change JWT Secret
Update in all services' `application.yml`:
```yaml
jwt:
  secret: YOUR_SECRET_KEY
```

### Change Kafka Bootstrap Servers
Update in services' `application.yml`:
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
```

## Port Summary

- **8761**: Eureka Server
- **8081**: Auth Service
- **8082**: API Gateway
- **8083**: Chat Service (WebSocket)
- **8084**: User Service
- **8085**: Message Service
- **3306**: MySQL
- **9092**: Kafka
- **2181**: Zookeeper

