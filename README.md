# Quadballholic - Microservices Architecture

*Note: This repository contains the distributed version of the Quadballholic backend. For the main architectural analysis, the Kata requirements, and the MVP Monolith, please visit our [Main Repository](https://github.com/denizbayan/Quadballholic-backend).*

## Architecture Diagram
![Microservices Architecture](./docs/images/microservices-arch.png)

## 🏗️ System Components
To satisfy the extreme **Elasticity (⭐⭐⭐⭐⭐)** requirement during live matches, we distributed the MVP monolith into an independent microservices architecture using Spring Cloud:

1. **API Gateway (`ApiGatewayApplication` - Port 8080):** * The single entry point for the frontend. 
   * Handles dynamic routing using Eureka. 
   * **WebSocket Routing:** As defined in our `application.yml`, it intercepts `/ws-quadball/**` traffic and routes it directly to the `live-game-events-service` via the Load Balancer (`lb://`).
2. **Discovery Server (`DiscoveryServerApplication` - Port 8761):** Netflix Eureka registry for dynamic service discovery.
3. **Core Backend Service:** Handles CRUD operations (Tournaments, Teams, Users). Connected to its own logical database.
4. **Live Match Service (`live-game-events-service`):** An independent service handling WebSockets and the in-memory game state engine.

---

## 🚀 How to Run (Local Environment)

To run the Distributed Architecture locally, you need Docker (for the databases) and Java 21. Since this is a distributed system, services must be started in a specific order.

### 1. Start the Databases
Open a terminal in the root folder and start the PostgreSQL containers:
```bash
docker-compose up -d
```

### 2. Start the Microservices (Via IDE or Terminal)
Open the project in your IDE (e.g., IntelliJ IDEA). You need to start the following Spring Boot applications in this exact order:

* Discovery Server: Run DiscoveryServerApplication.java (Starts on port 8761). Wait for it to be fully running.

* Core Backend Service: Run the main application class for the core domain.

* Live Game Events Service: Run the main application class for the live matches.

* API Gateway: Run ApiGatewayApplication.java (Starts on port 8080).

Alternatively, you can run 
```bash
./mvnw spring-boot:run
```
inside each service's respective directory.

Once all services are up, they will register with Eureka (http://localhost:8761), and the API Gateway (http://localhost:8080) will be ready to route frontend requests transparently!
