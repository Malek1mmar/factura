# 📄 Factura Backend – Architecture & Security

## 🚀 Overview

This project is a backend service for managing invoices for small businesses (cafés, restaurants, shops) in Tunisia.

It is built with:

* **Spring Boot** (REST API)
* **Keycloak** (Authentication & Identity)
* **PostgreSQL** (Database)
* **Hibernate / JPA** (ORM)

---

# 🔐 Security Architecture

## 🎯 Principle

We follow a **modern stateless security architecture**:

```
Client → Keycloak → JWT → Spring Boot API
```

---

## 🧠 Responsibilities

### 🔑 Keycloak

Handles:

* User authentication
* Login / password management
* Token (JWT) generation

👉 Keycloak answers:

```
WHO are you?
```

---

### ⚙️ Spring Boot (Resource Server)

Handles:

* JWT validation
* Securing endpoints
* Extracting user information

👉 Spring answers:

```
WHAT are you allowed to do?
```

---

## 🔐 JWT Flow

1. User authenticates via Keycloak
2. Keycloak returns an `access_token` (JWT)
3. Client calls API with:

```
Authorization: Bearer <token>
```

4. Spring Boot validates token using:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8081/realms/factura-app
```

---

## ⚠️ Important Notes

* ❌ No `client_secret` is used in backend (resource server mode)
* ✔ Backend only validates JWT
* ✔ Authentication is fully externalized

---

# 👤 User Management

## 🎯 Problem

Keycloak manages authentication, but **does not store business data**.

👉 Therefore, we implement **user synchronization**.

---

## 🔄 User Sync Strategy

When a request hits the API:

```java
if (user not found in DB) {
    create user from JWT
}
```

---

## 🧾 User Entity

```java
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String keycloakId;

    private String email;

    private String username;
}
```

---

## 🔑 JWT Mapping

| JWT Claim            | Usage                          |
| -------------------- | ------------------------------ |
| `sub`                | keycloakId (unique identifier) |
| `preferred_username` | username                       |
| `email`              | email                          |

---

## 🧠 Design Decision

```
Keycloak = Identity
Database = Business representation
```

---

# 🏢 Business Architecture (Multi-Tenant)

## 🎯 Goal

Support multiple businesses:

* Café A
* Restaurant B
* Shop C

👉 Each with isolated data.

---

## 🧱 Core Model

```
User
  ↓
Membership
  ↓
Organization
```

---

## 🏢 Organization

Represents a business:

```java
@Entity
public class Organization {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String address;
}
```

---

## 🔗 Membership (Key Concept)

Represents:

```
User ↔ Organization + Role
```

```java
@Entity
public class Membership {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Organization organization;

    private String role; // ADMIN, EMPLOYEE
}
```

---

## 🧠 Why Membership?

Because:

* A user can belong to multiple organizations
* A user can have different roles per organization

---

## 📦 Example

```
Ali → Café Roma → ADMIN
Ali → Pizza House → EMPLOYEE
```

---

# 🗄️ Database Strategy

## Development Mode

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

👉 Auto-creates tables

---

## Production Mode (later)

* Use Flyway
* `ddl-auto: validate`

---

## ⚠️ Naming Convention

Avoid reserved SQL keywords:

❌ `user`
✔ `app_user`

---

# 🧪 Available Endpoints

## 🔓 Public

```
GET /public/hello
```

---

## 🔐 Secure

```
GET /api/test/secure
```

Requires JWT

---

## 👤 Current User

```
GET /api/me
```

Returns:

```json
{
  "id": "...",
  "keycloakId": "...",
  "email": "...",
  "username": "..."
}
```

---

# 🧠 Key Concepts Recap

```
Authentication → Keycloak
Authorization → Spring Security
Business Data → PostgreSQL
```

---

# 🚀 Next Steps

* Add Organization management API
* Add Membership roles & permissions
* Implement invoice module:

    * upload
    * OCR
    * classification

---

# 💡 Final Thought

This architecture is:

* ✅ Scalable
* ✅ Secure
* ✅ SaaS-ready
* ✅ Multi-tenant capable

---

# 👨‍💻 Author

Backend built with a focus on clean architecture and production-ready patterns.
