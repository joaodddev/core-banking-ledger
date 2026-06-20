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

## API Endpoints

### Accounts

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/accounts` | Open a new account |
| DELETE | `/api/v1/accounts/{accountId}` | Close an account |

### Transactions

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/accounts/{accountId}/credit` | Credit an account |
| POST | `/api/v1/accounts/{accountId}/debit` | Debit an account |
| POST | `/api/v1/accounts/{accountId}/transfer` | Transfer between accounts |

## Idempotency

Financial endpoints (`credit`, `debit`, `transfer`) require an `Idempotency-Key` header.
Duplicate requests with the same key return `409 Conflict`.
Keys are stored in Redis with 24h TTL.