# 📄 Factura Backend – Architecture & Security

## 🚀 Overview

This project is a backend service for managing invoices for small businesses (cafés, restaurants, shops) in Tunisia. It is built using **Hexagonal Architecture (Ports & Adapters)** to ensure a clean separation between business logic and technical concerns.

Built with:

* **Spring Boot 3.5.x** (REST API & Resource Server)
* **Java 21**
* **Keycloak** (Authentication & Identity)
* **PostgreSQL** (Database)
* **Hibernate / JPA** (Persistence)
* **Lombok** (Boilerplate reduction)

---

# 🏗️ Hexagonal Architecture

The project follows a strict Hexagonal (Clean) Architecture:

### 📦 Core Layer (`com.example.fatoura.core`)
Independent of any framework or infrastructure.
*   **Domain**: Business entities (`Invoice`, `User`, `Organization`, `Membership`, `InvoiceStatus`) and logic.
*   **Application**:
    *   **Inbound Ports**: Interfaces defining use cases (`CreateOrganizationUseCase`, `GetInvoicesUseCase`, `GetInvoiceUseCase`, `SyncUserUseCase`, `UploadInvoiceUseCase`).
    *   **Outbound Ports**: Interfaces for external dependencies (`InvoiceRepository`, `UserRepository`, `OrganizationRepository`, `MembershipRepository`, `FileStoragePort`).
    *   **Services**: Business logic implementations that coordinate between ports.

### 🔌 Infrastructure Layer (`com.example.fatoura.infrastructure`)
Contains technical implementations.
*   **Web**: REST Controllers, Security configuration, DTOs, and Web-to-Domain mappers.
*   **Persistence**: JPA Repositories, Entities, and Persistence Adapters (mapping between DB and Domain).
*   **Storage**: Adapters for file storage (currently using `LocalFileStorageAdapter`).

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
*   **UserArgumentResolver**: Automatically resolves the current authenticated `User` from the JWT token in controller methods.
*   **SecurityConfig**: Configures OAuth2 Resource Server with JWT decoding.

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
Managed within the scope of an Organization. Statuses: `UPLOADED`, `PROCESSED`, etc.

---

# 🧾 Invoice Management

## 📂 Features
*   **Secure Upload**: Upload invoice PDFs/images tied to an organization.
*   **Access Control**: Strict membership checks to ensure users only access invoices for their organizations.
*   **Local Storage**: Invoices are stored locally in the `uploads/` directory with unique UUID prefixes.

---

# 🧪 Available Endpoints

### 🧾 Invoices (`/api/invoices`)
*   `POST /upload`: Upload an invoice (requires `organizationId` and `file`).
*   `GET /`: List all invoices for a specific organization (requires `organizationId`).
*   `GET /{id}`: Retrieve details of a specific invoice.

### 🏢 Organizations (`/api/organizations`)
*   `POST /`: Create a new organization.
*   `GET /`: List organizations the current user belongs to.
*   `GET /{id}`: Retrieve organization details.

### 🧪 Testing
*   `GET /api/test/me`: Returns current user details extracted from JWT.

---

# 🚀 Roadmap

*   [ ] **OCR Engine**: Integrate with an OCR service to extract invoice data (vendor, date, amounts, VAT).
*   [ ] **Dashboard**: Statistics on spending and invoice counts.
*   [ ] **Multi-Role**: Implement "Admin" vs "Viewer" roles within an organization.
*   [ ] **Cloud Storage**: Implement S3/MinIO adapter for `FileStoragePort`.

---

# 👨‍💻 Author

Backend built with a focus on clean architecture, scalability, and production-ready patterns.
