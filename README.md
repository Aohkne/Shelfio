# Shelfio — RESTful Library Management System

A Spring Boot 3 backend for managing books, members, and borrowings with JWT authentication and role-based access control.

![Java](https://img.shields.io/badge/Java-21-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)

---

## Features

- **JWT Authentication** — stateless login, token refresh, and dual-token logout
- **Role-Based Access Control** — ADMIN, LIBRARIAN, MEMBER roles with method-level security
- **Book Management** — CRUD with dynamic filtering, pagination, and ISBN validation
- **Member Management** — profile view, status suspend/reactivate
- **Borrowing System** — borrow/return flows with quantity tracking and overdue detection
- **Admin Dashboard** — parallel stats queries with CompletableFuture
- **OpenAPI / Swagger UI** — grouped API docs for each domain
- **Field-Level Validation** — detailed validation error responses

---

## Getting Started

### Prerequisites

- Java 21+ (for local run without Docker)
- Docker & Docker Compose

### Run with Docker

```bash
# 1. Clone the repository
git clone https://github.com/your-username/shelfio.git
cd shelfio

# 2. Configure environment
cp .env.example .env
# Edit .env to set a strong JWT_SECRET

# 3. Start everything
docker-compose up --build
```

The API will be available at `http://localhost:8080/api/backend-service`.

### Run Locally (without Docker)

1. Start a PostgreSQL instance with a database named `postgres_db`
2. Configure `backend/src/main/resources/application.yml` or set environment variables
3. Run:

```bash
cd backend
./mvnw spring-boot:run
```

---

## Default Admin Credentials

| Field    | Value      |
|----------|------------|
| Username | `admin`    |
| Password | `Login123@`|

---

## API Documentation

Swagger UI: **http://localhost:8080/api/backend-service/swagger-ui/index.html**

### Endpoint Overview

| Group         | Base Path           | Auth Required | Min Role    |
|---------------|---------------------|---------------|-------------|
| Authentication| `/auth/**`          | No            | —           |
| Book          | `/api/books/**`     | Partial       | MEMBER      |
| Member        | `/api/members/**`   | Yes           | MEMBER      |
| Borrowing     | `/api/borrowings/**`| Yes           | LIBRARIAN   |
| Admin         | `/api/admin/**`     | Yes           | ADMIN       |

### Key Endpoints

```
POST   /auth/register              Register a new user
POST   /auth/login                 Login (email or username)
POST   /auth/refresh-token         Refresh access token
POST   /auth/logout                Logout (invalidate tokens)

GET    /api/books                  List books (public)
GET    /api/books/{id}             Get book by ID (public)
POST   /api/books                  Create book (ADMIN, LIBRARIAN)
PUT    /api/books/{id}             Update book (ADMIN, LIBRARIAN)
DELETE /api/books/{id}             Delete book (ADMIN)

GET    /api/members                List all members (ADMIN, LIBRARIAN)
GET    /api/members/me             My profile
GET    /api/members/{id}           Get member by ID
PATCH  /api/members/{id}/status    Update member status (ADMIN)

POST   /api/borrowings             Borrow a book (LIBRARIAN, ADMIN)
PATCH  /api/borrowings/{id}/return Return a book (LIBRARIAN, ADMIN)
GET    /api/borrowings/overdue     List overdue borrowings (ADMIN, LIBRARIAN)

GET    /api/admin/stats            Dashboard statistics (ADMIN)
```

---

## Database Schema

```
users          — id (String), username, email, password, fullname, dob
roles          — name (PK), description
permissions    — name (PK), description
members        — id (UUID), user_id FK, member_code, status, join_date
books          — id (UUID), title, author, isbn (unique), publisher, quantity, available_qty, added_date
borrowings     — id (UUID), member_id FK, book_id FK, borrow_date, due_date, return_date, status
tokens         — id (JTI), expiry_time
```

---

## Environment Variables

| Variable           | Default                          | Description                     |
|--------------------|----------------------------------|---------------------------------|
| `DB_URL`           | `jdbc:postgresql://...`          | PostgreSQL JDBC URL             |
| `DB_USER`          | `postgres_user`                  | Database username               |
| `DB_PASS`          | `postgres_pass`                  | Database password               |
| `JWT_SECRET`       | (insecure default)               | HS256 signing secret (min 32 chars) |
| `JWT_ACCESS_EXPIRY`| `900`                            | Access token TTL (seconds)      |
| `JWT_REFRESH_EXPIRY`| `604800`                        | Refresh token TTL (seconds)     |

> **Important**: Always set a strong `JWT_SECRET` in production.