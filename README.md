## Microservices Architecture
Following the successful validation of our MVP, we transitioned the project into a Microservices Architecture. This shift was designed to address scaling limitations while leveraging the strict boundaries established during the modular phase. Each domain now operates as an independent service, communicating via REST and focusing on high availability.

### Architectural Highlights
The transition was seamless thanks to our original "contract-first" mindset. We evolved our core patterns to handle the challenges of a distributed system:

* **API Gateway (Evolution of Facades)**: The Facade layer has evolved into a dedicated API Gateway. It serves as the single entry point, handling request routing and security. It continues to orchestrate complex workflows that span multiple microservices, shielding the frontend from the underlying distributed complexity.

* **Feign Clients & DTOs (Evolution of SharedContracts)**: The interfaces in SharedContracts were transformed into Feign Clients. Service A no longer calls Service B via a local bean; instead, it uses declarative REST clients. This allowed us to swap communication protocols without touching core business logic.

* **Distributed Resilience (The "Best-Effort Rollback" Pattern)**: One of our key architectural choices was how to handle cross-service transactions (e.g., creating a Tournament and its associated Matches). Instead of over-engineering a full Saga Pattern with message brokers, we applied the Pareto Principle (80/20 rule):

* **Strategic Inconsistency**: We acknowledge that in a distributed world, strict ACID transactions are costly. We moved toward Eventual Consistency.

* **Manual Compensation**: We implemented a "Best-Effort Rollback" logic using try-catch blocks and manual compensating calls (e.g., deleting orphaned matches if the tournament creation fails). This mitigates 99% of consistency issues (network timeouts, validation errors) with minimal architectural overhead.

### Folder Structure (Distributed Partitioning)
The project is organized as a monorepo of independent Spring Boot services:
```
Quadballholic-Microservices/
├── docker-compose.yml          # Orchestrates all services and infrastructure
├── config-server/              # Centralized configuration management
├── gateway/                    # API Gateway & Security Filter
├── discovery-server/           # Service Registry (Eureka)
│
├── auth-service/               # Identity Provider & JWT Issuance
├── match-service/              # Manages Matches & Match Officials
├── live-event-service/         # WebSockets & Real-time updates
├── player-service/             # Player profiles and statistics
├── reservation-service/        # Booking logic
├── team-service/               # Team & Tournament management (Orchestrator)
└── user-service/               # User profiles and preferences
```
Each microservice is self-contained with its own isolated database, ensuring that a schema change in one domain never impacts another.

### Non-Functional Requirements
* **Elastic Scalability**: We can scale the live-event-service independently during high-traffic match days.

* **Fault Tolerance**: The system lacks a single point of failure. If the reservation-service is down, the rest of the platform remains functional.

* **Pragmatic Consistency**: By choosing a manual compensation strategy over complex distributed transactions, we maintained high development velocity while keeping the data "clean enough" for a production-grade MVP.

* **Continuous Deployment**: Smaller codebases allow for faster CI/CD cycles and targeted updates.

---

### Quick Start
To launch the ecosystem, you need Docker Desktop. Note that running multiple Spring Boot containers requires significant RAM.

Configure the .env files in the root and service directories.

Execute: docker-compose up --build

Monitor service health via the Discovery Server at localhost:8761.

The system includes an automatic Data Seeder that populates the distributed databases with the standard Admin and Team Manager test accounts.
