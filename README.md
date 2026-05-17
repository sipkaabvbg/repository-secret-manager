# CI/CD Repository Credential Service

## Overview
This project is a subsystem of a CI/CD platform. It provides a web service and a graphical user interface (GUI) designed to securely manage repository URLs and the secrets required for authenticating against them. 

**Key Features:**
- Managing Git repository URLs (CRUD operations).
- Managing authentication secrets/credentials.
- Linking repositories with their respective secrets.
- **Validating repository credentials** (checking if the connection to the repository is successful using the provided secret).

## Tech Stack

**Backend:**
- Java 25
- Spring Boot 
- H2 Database (In-memory, chosen for zero-setup execution)

**Frontend:**
- SAPUI5
- Node.js 

---

## Prerequisites
To run this project locally, you need to have the following installed:
- **Java 25** (or compatible JDK)
- **Maven** (or you can use the included Maven wrapper `./mvnw`)
- **Node.js** (v18 or higher) & **npm** (for the SAPUI5 frontend)

---

## How to Run

### 1. Start the Backend (Spring Boot)
- The backend runs on http://localhost:8081. The H2 database is in-memory and will initialize automatically.
- Navigate to the backend directory:

cd repository-secret-service

- Run the application using the Maven Wrapper (no local Maven installation required!):
- For Mac/Linux:

 ./mvnw spring-boot:run

- For Windows:

 mvnw.cmd spring-boot:run

- (Optional) You can access the H2 database console at http://localhost:8081/h2-console.
### 2. Start the Frontend (SAPUI5)
- The frontend uses UI5 Tooling and runs on a local development server.
- Open a new terminal and navigate to the repository-secret-ui directory:

cd repository-secret-ui

- Install the required dependencies:


npm install

- Start the UI5 development server:


npm start

- The application should automatically open in your default browser (usually at http://localhost:8080/index.html or a proxy port like 8081).



