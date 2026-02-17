# Quadballholic backend
*Note: This repository contains the microservices backend for the Quadballholic project. For the main architectural analysis, the Kata requirements, and the monolithic backend implementations, please visit our [Main Repository](https://github.com/denizbayan/Quadballholic-backend).*

## Microservices Architecture
We chose Microservices as the distributed alternative to modular monolith backend. This shift leveraged the strict boundaries established during the modular phase. The code was indeed developed with strict separation of duty in mind, leading to a straight-forward evolution towards services. Each domain now operates as an independent service communicating via REST. 

### Architectural Highlights
The transition was seamless, thanks to our “contract-first” mindset. We evolved our core patterns to address the challenges of a distributed system:

* **API Gateway**: Serves as the single entry point, handling request routing so that the frontend remains decoupled from backend implementation details. It doesn’t need to know which service handles a request or on which port it runs, enabling independent evolution of services.

* **Feign Clients & DTOs (Evolution of SharedContracts and Facade)**: Interfaces in SharedContracts were transformed into Feign Clients. Service A no longer calls Service B via a local bean; instead, it uses declarative REST clients. This abstraction allows us to swap communication protocols without modifying core business logic.

* **Distributed Resilience (The "Best-Effort Rollback" Pattern)**: Handling cross-service transactions—such as creating a Tournament and its associated Matches—was a key architectural challenge. Rather than implementing a full Saga Pattern with message brokers, we applied the **Pareto Principle (80/20 rule)**:
  * **The Problem**: In a monolith, `@Transactional` ensures that if the creation of the last match fails, all related database changes are rolled back. In a microservices environment, `@Transactional` does not span remote calls, leading to potential inconsistencies between services.
  * **Strategic Inconsistency**: We embraced Eventual Consistency, accepting that strict ACID transactions are costly in a distributed system.
  * **Manual Compensation**: Orphan matches (created when tournament creation fails) remain in the database but are invisible to the frontend. To mitigate inconsistencies, we implemented a Best-Effort Rollback using try-catch blocks and compensating calls to delete orphaned matches when necessary. This approach addresses approximately 99% of consistency issues—such as network timeouts or validation errors—achieving high reliability with minimal overhead.

### Folder Structure (Distributed Partitioning)
The project is organized as a monorepo of independent Spring Boot services. The division is almost the same as in the monolithic architecture. We decided to unify user and auth, since they are highly coupled.
```
Quadballholic-Microservices/
├── docker-compose.yml          
├── apiGateway/                 
├── discovery-server/      # Service Registry (Eureka)
│
├── userAuth/
├── common-library/           
├── match/
├── live-game-events/      # WebSockets & Real-time updates
├── player/
├── reservation/           # Booking logic
├── team/
└── tournament/            # contains orchestration service
```
Each microservice is self-contained with its own isolated database, ensuring that a schema change in one domain never impacts another.

### Non-Functional Requirements
* **Elastic Scalability**: We can scale the live-event-service independently during high-traffic match days.
* **Fault Tolerance**: The system lacks a single point of failure. If the reservation-service is down, the rest of the platform remains functional.
* **Pragmatic Consistency**: By choosing a manual compensation strategy over complex distributed transactions, we maintained high development velocity while keeping the data "clean enough" for a production-grade MVP.

---

### Quick Start
To launch the ecosystem, you need Docker Desktop. Note that running multiple Spring Boot containers could require a significant amount of RAM.

Configure the .env files in the root and service directories.

Execute: `docker-compose up --build`

Monitor service health via the Discovery Server at `localhost:8761`.

The system includes an automatic Data Seeder that populates the distributed databases with the standard Admin and Team Manager test accounts, as specified in the monolithic readme.
