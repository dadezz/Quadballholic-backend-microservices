# 🕸️ Quadballholic - Microservices Architecture
*Note: This repository contains the distributed version of the Quadballholic backend. For the main architectural analysis and the MVP Monolith, please visit our [Main Repository](https://github.com/denizbayan/Quadballholic-backend).*

## How to run
```bash
docker-compose up --build
```

## Architecture Diagram
![Microservices Architecture](../docs/images/microservices-arch.png)

## 🏗️ System Components
To satisfy the extreme **Elasticity (⭐⭐⭐⭐⭐)** requirement during live matches, we distributed the system using Spring Cloud:

1. **API Gateway (`ApiGatewayApplication` - Port 8080):** * The single entry point for the frontend. 
   * Handles dynamic routing using Eureka. 
   * **WebSocket Routing:** As defined in our `application.yml`, it intercepts `/ws-quadball/**` traffic and routes it directly to the `live-game-events-service` via the Load Balancer (`lb://`).
2. **Discovery Server (`DiscoveryServerApplication` - Port 8761):** Netflix Eureka registry for dynamic service discovery.
3. **Core Backend Service:** Handles CRUD operations (Tournaments, Teams, Users). Connected to its own logical database.
4. **Live Match Service (`live-game-events-service`):** An independent service handling WebSockets and the in-memory game state engine.

## ✅ Pros vs ❌ Cons
**Pros:**
* **Elasticity:** We can spawn multiple instances of the `Live Match Service` during the World Cup, while keeping only 1 instance of the `Core Service`.
* **Fault Tolerance:** If the Core Service goes down, the Live Match Service keeps broadcasting current scores via WebSockets.
* **Database Isolation:** Enforced via our `init-dbs.sh` in docker-compose, preventing domains from corrupting each other's data.

**Cons:**
* **High Operational Complexity:** Requires orchestration (Docker network, Eureka, Gateway).
* **Distributed Data:** Keeping data consistent across services requires complex synchronization or event-driven patterns.
