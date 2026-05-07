# 📄 Factura Backend – Architecture & Security

## 🚀 Overview

This project is a backend service for managing invoices for small businesses (cafés, restaurants, shops) in Tunisia. It is built using **Hexagonal Architecture (Ports & Adapters)** to ensure a clean separation between business logic and technical concerns.

Built with:

* **Spring Boot 3** (REST API & Resource Server)
* **Keycloak** (Authentication & Identity)
* **PostgreSQL** (Database)
* **Hibernate / JPA** (Persistence)
* **Lombok** (Boilerplate reduction)

---

# 🏗️ Hexagonal Architecture

The project follows a strict Hexagonal (Clean) Architecture:

### 📦 Core Layer (`com.example.fatoura.core`)
Independent of any framework or infrastructure.
*   **Domain**: Business entities (`Invoice`, `User`, `Organization`) and logic.
*   **Application**:
    *   **Inbound Ports**: Interfaces defining use cases (`UploadInvoiceUseCase`).
    *   **Outbound Ports**: Interfaces for external dependencies (`InvoiceRepository`, `FileStoragePort`).
    *   **Services**: Business logic implementations that coordinate between ports.

### 🔌 Infrastructure Layer (`com.example.fatoura.infrastructure`)
Contains technical implementations.
*   **Web**: REST Controllers, Security configuration, and Web-to-Domain mappers.
*   **Persistence**: JPA Repositories, Entities, and Persistence Adapters (mapping between DB and Domain).
*   **Storage**: Adapters for file storage (local file system, cloud storage).

---

# 🔐 Security Architecture

## 🎯 Principle

We follow a **modern stateless security architecture**:

```
Client → Keycloak → JWT → Spring Boot API
```

### 🔑 Keycloak
Handles Identity management (Who are you?).

### ⚙️ Spring Boot (Resource Server)
Handles Authorization (What are you allowed to do?) and JWT validation.

---

# 🏢 Business Architecture (Multi-Tenant)

## 🎯 Goal
Support multiple businesses with isolated data.

## 🧱 Core Model

```
User ↔ Membership ↔ Organization ↔ Invoice
```

### 👤 User
Synchronized from Keycloak JWT on first request.

### 🏢 Organization
Represents a business entity.

### 🔗 Membership
Connects a User to an Organization with specific roles.

### 🧾 Invoice
Managed within the scope of an Organization.

---

# 🧾 Invoice Management

## 📤 Upload Flow
1. **Controller**: Receives `MultipartFile` and `organizationId`.
2. **Service**:
    * Validates User membership in the Organization.
    * Uses `FileStoragePort` to store the physical file.
    * Uses `InvoiceRepository` to persist metadata.
3. **Persistence**: `InvoicePersistenceAdapter` maps the domain model to `InvoiceEntity` for JPA storage.

---

# 🗄️ Database Strategy

* **Development**: `ddl-auto: update` for rapid iteration.
* **Naming**: Uses `app_user` for users to avoid SQL keyword conflicts.
* **Entities**: Isolated in `infrastructure.persistence.entity` to prevent leaking persistence details into the core domain.

---

# 🧪 Available Endpoints

### 🧾 Invoices
*   `POST /api/invoices/upload` (Secure): Upload an invoice for a specific organization.

### 🏢 Organizations
*   `POST /api/organizations` (Secure): Create a new organization.
*   `GET /api/organizations` (Secure): List organizations for current user.

### 👤 User
*   `GET /api/me` (Secure): Current user profile.

---

# 🚀 Next Steps

* Implement **OCR engine** for automatic data extraction from invoices.
* Add **Invoice Search & Filtering**.
* Implement **Role-based Access Control (RBAC)** via Membership roles.
* Add **Export to PDF/Excel** features.

---

# 👨‍💻 Author

Backend built with a focus on clean architecture, scalability, and production-ready patterns.
