# Core Banking Ledger

Enterprise-grade banking ledger built with **Java 21**, **Spring Boot 3**, **DDD**, and **Clean Architecture**.

## Domain concepts

- **Account** — aggregate root with credit, debit and close behaviors
- **Transaction** — records every ledger movement (transfer, credit, debit)
- **Money** — immutable value object with currency validation (BRL)
- **Domain Events** — published to Kafka on every state change

## Stack

| Layer          | Technology                        |
|----------------|-----------------------------------|
| Language       | Java 21                           |
| Framework      | Spring Boot 3.3.4                 |
| Database (dev) | H2 in-memory                      |
| Messaging      | Apache Kafka                      |
| Cache          | Redis                             |
| Build          | Maven                             |

