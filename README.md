# Finance API System

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-database-blue)
![Maven](https://img.shields.io/badge/build-Maven-red)

A secure Spring Boot backend for core banking operations — creating accounts, handling deposits, withdrawals and transfers, and keeping a full transaction history — all protected behind JWT-based authentication.

## Overview

Finance API System models the core operations of a simple banking platform as a layered Spring Boot application (controller → service → repository) backed by PostgreSQL. Spring Security enforces stateless, token-based authentication on every route except login. Business rules — balance validation, insufficient-funds checks, self-transfer prevention — live in the service layer, and every deposit, withdrawal, and transfer is recorded as its own transaction entry for auditing.

## Features

- **Account management** — create, view, update, and delete bank accounts
- **Deposits & withdrawals** with balance validation (no negative balances, no overdrawing)
- **Account-to-account transfers** with insufficient-funds and same-account checks
- **Transaction history** — full list, or paginated and sorted with the most recent first
- **JWT authentication** — stateless login that issues a bearer token for all protected routes
- **Centralized error handling** with consistent HTTP status codes for not-found, auth, and business-rule errors
- **Request validation** via Jakarta Bean Validation (positive amounts, required fields, etc.)
- **BCrypt password hashing**
- **Integration tests** using MockMvc against an in-memory H2 database

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.0.3 |
| Data access | Spring Data JPA / Hibernate |
| Database | PostgreSQL (H2 for tests) |
| Security | Spring Security, JWT (jjwt 0.12.5), BCrypt |
| Validation | Jakarta Bean Validation |
| Build tool | Maven |
| Testing | JUnit 5, MockMvc |
| Boilerplate | Lombok |

## Project Structure

```
financeapisystem/
├── Config/         # Jackson (JSON) configuration
├── Controller/     # REST endpoints — AccountController, AuthController
├── Dtos/           # Request/response payloads
├── Exception/      # Custom exceptions + global exception handler
├── Modals/         # JPA entities — Account, Transaction, Users
├── Respository/    # Spring Data JPA repositories
├── Security/       # JWT util, JWT filter, Spring Security config
└── Service/        # Business logic (AccountService + implementation)
```

## API Reference

All endpoints below except `/auth/login` require an `Authorization: Bearer <token>` header.

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/login` | Authenticate with username/password and receive a JWT |

### Accounts

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/accounts` | Create a new account |
| GET | `/api/accounts/{id}` | Get account details |
| PUT | `/api/accounts/{id}` | Update an account's name |
| DELETE | `/api/accounts/{id}` | Delete an account |
| POST | `/api/accounts/deposit/{id}?money={amount}` | Deposit funds into an account |
| POST | `/api/accounts/withdraw/{id}?amount={amount}` | Withdraw funds from an account |
| POST | `/api/accounts/transfer?fromId={id}&toId={id}&money={amount}` | Transfer funds between two accounts |

### Transactions

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/accounts/transactions/{accountId}` | Full transaction history for an account |
| GET | `/api/accounts/{accountId}/transactions?page={n}&pageSize={n}` | Paginated history, newest first (alias: `/api/accounts/transaction/{accountId}`) |

## Getting Started

### Prerequisites

- Java 25
- Maven (or the included `mvnw` wrapper)
- A running PostgreSQL instance

### Environment Variables

Configuration is read from environment variables, each with a local-friendly default:

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/bankDB` | PostgreSQL connection string |
| `DB_USERNAME` | *(empty)* | Database username |
| `DB_PASSWORD` | *(empty)* | Database password |
| `DDL_AUTO` | `update` | Hibernate schema generation strategy |
| `SHOW_SQL` | `false` | Log generated SQL statements |
| `JWT_SECRET` | *(dev key)* | Secret used to sign JWTs — override this in production |
| `JWT_EXPIRATION_MS` | `3600000` | Token lifetime in milliseconds (1 hour) |

### Run locally

```bash
git clone https://github.com/jayRajput15/Finance-System-api.git
cd Finance-System-api/financeapisystem
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

### Run tests

```bash
./mvnw test
```

Tests run against an in-memory H2 database, so no PostgreSQL setup is required.

## Authentication Flow

1. A matching row must already exist in the `users` table (with a BCrypt-hashed password) — there's no self-service registration endpoint yet, so users are currently provisioned directly in the database.
2. Log in to receive a token:

   ```bash
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "jay", "password": "yourpassword"}'
   ```

3. Send the token on every subsequent request:

   ```bash
   curl http://localhost:8080/api/accounts/1 \
     -H "Authorization: Bearer <token>"
   ```

## Error Handling

| Exception | HTTP Status |
|---|---|
| `AccountNotFoundException` | 404 Not Found |
| `AuthenticationFailedException` | 401 Unauthorized |
| `InvalidTransactionException` / `InsufficientBalanceException` | 400 Bad Request |
| Validation errors (`@Valid`, constraint violations) | 400 Bad Request, with field-level messages |

## Roadmap

- [ ] User self-registration endpoint
- [ ] Role-based authorization (currently every authenticated user has equal access)
- [ ] Dockerfile / docker-compose for one-command local setup
