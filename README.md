# CI/CD Repository Credential Service

## Overview
This project is a subsystem of a CI/CD platform. It provides a web service and a graphical user interface (GUI) designed to securely manage repository URLs and the secrets required for authenticating against them. 

## Features
- **Repository Management:** Add, list, and delete external repositories (e.g., GitHub, GitLab).
- **Secrets Management:** Securely add, link, and delete authentication secrets.
- **Dynamic Relationship:** Link repositories to secrets or keep them public.
- **Real-time Validation:** Validate if a repository's linked secret provides successful authentication (read access) against the external provider (GitHub API).
- **Responsive UI:** Clean and dynamic SAPUI5 interface for seamless user experience.

## Tech Stack
- **Backend:** Java 25, Spring Boot 4, Spring Data JPA, H2 Database (In-Memory)
- **Frontend:** JavaScript, SAPUI5, Node.js
- **Containerization:** Docker & Docker Compose
- **Build Tool:** Maven

## Security Architecture & Data Privacy
In a production-ready CI/CD system, secrets must never be stored in plain text. In this project, **Personal Access Tokens (PAT) are stored symmetrically encrypted** in the database using the Advanced Encryption Standard (AES). 

This is implemented transparently via a JPA `AttributeConverter`. The application logic operates with plain objects, while data at rest remains secure against unauthorized database access. 
*(Note: For the scope of this exercise, the AES encryption key is hardcoded. In a real environment, it should be fetched from a secure vault or environment variable).*

In addition, standard sequential IDs have been replaced with **UUIDs** to prevent Insecure Direct Object Reference (IDOR) vulnerabilities.

## Why only GitHub PAT (Tokens)?
The system currently supports authentication via **Personal Access Tokens (PAT)**. Since GitHub deprecated basic password authentication for Git operations, PAT is the industry standard for API and HTTPS Git operations. 
Implemented repository credential validation logic, supporting secure authentication via GitHub REST API and enabling future extensibility for SSH-based validation mechanisms.

## Database Relations & Architecture
- **Many-to-One (Repositories ➔ Secrets):** The system uses a `@ManyToOne` Hibernate relationship. This allows authentication credentials to be centralized and shared across different repository tracks.
- **Optional Secret (Nullable Foreign Key):** If a repository is **Public**, the user can save it without choosing a credential from the dropdown menu. In this case, the frontend sends a `null` payload, and the database stores a `NULL` value in the `secret_id` column. The UI automatically displays these entries as `No Secret (Public)`.


## How to Run

## Prerequisites
To run this project locally, you need to have the following installed:
- **Java 25** (or compatible JDK)
- **Maven** (or you can use the included Maven wrapper `./mvnw`)
- **Node.js** (v18 or higher) & **npm** (for the SAPUI5 frontend)
- **Docker** (Docker and Docker Compose installed on your machine)

---

### Option 1: Using Docker (Recommended)
You can run the entire stack (Frontend + Backend) using Docker Compose.

- Clone the repository:
   ```bash
   git clone https://github.com/sipkaabvbg/repository-secret-manager.git
   cd repository-secret-manager
Build and start the containers:

Run the following command in the console to build and start the containers:

docker-compose up --build

- Access the application:
Frontend (UI5): http://localhost:8080
Backend API: http://localhost:8081/api/v1/swagger-ui/index.html#/


### Option 2: Running Locally (Manual Setup)
### Start the Backend (Spring Boot)
Ensure you have Java 25 and Maven installed.
- The backend runs on http://localhost:8081. The H2 database is in-memory and will initialize automatically.
- Navigate to the backend directory:

cd repository-secret-service

- Run the application using the Maven Wrapper (no local Maven installation required!):
- For Mac/Linux:

 ./mvnw spring-boot:run

- For Windows:

 mvnw.cmd spring-boot:run

- (Optional) You can access the H2 database console at http://localhost:8081/h2-console.
### Start the Frontend (SAPUI5)
Ensure you have Node.js installed.
- The frontend uses UI5 Tooling and runs on a local development server.
- Open a new terminal and navigate to the repository-secret-ui directory:

cd repository-secret-ui

- Install the required dependencies:

npm install

- Start the UI5 development server:

npm start

- The application should automatically open in your default browser (usually at http://localhost:8080/index.html or a proxy port like 8081).

## API Endpoints Summary

- GET `/api/v1/repositories` - Fetch all repositories
- POST `/api/v1/repositories` - Add a new repository
- DELETE `/api/v1/repositories/{id}` - Delete a repository
- GET `/api/v1/secrets` - Fetch all secrets
- POST `/api/v1/secrets` - Add a new secret
- DELETE `/api/v1/secrets/{id}` - Delete a secret
- POST `/api/v1/validate` - Validates the repository URL against the given Secret ID
