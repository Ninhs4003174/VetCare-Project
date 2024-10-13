# RMIT COSC2299 SEPT Major Project Instructions

**DO NOT MODIFY THIS FILE**

## IMPORTANT

- You are not allowed to make this repository public or share its content out side of the github organization created for this course.

## Setup your environment 
You will need to have in your system

- Java 17.0 or higher
- Apache Maven
- IDE or Editor

Other tools will be required to complete the project (e.g., Docker)

## Run Instructions

Certainly! Below is an updated version of the 

README.md

 file that includes instructions for running the application using Maven, with Docker as an optional step.

```markdown
# Spring Boot Application with PostgreSQL and Flyway

## Prerequisites

- Java 17
- Maven
- PostgreSQL (optional if using Docker)

## Setup Instructions

### 1. Clone the Repository

```sh
git clone <repository-url>
cd <repository-directory>
```

### 2. Update Database Details

Open the `src/main/resources/application.properties` file and update the database details with your own:

```properties
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
```

### 3. Create the Database

Ensure that the database is created before running the application. You can create the database manually using a PostgreSQL client or script:

```sh
psql -U your_db_username -c "CREATE DATABASE your_db_name;"
```

### 4. Build and Run the Application with Maven

Use Maven to build and run the application:

```sh
mvn clean install
mvn spring-boot:run
```

### 5. Access the Application

Once the application is running, you can access it at `http://localhost:8080`.

## Optional: Run with Docker Compose

If you prefer to use Docker, you can follow these steps:

### 1. Update Docker Compose Configuration

Ensure the `docker-compose.yml` file has the correct database details:

```yaml
version: '3.8'
services:
  db:
    image: postgres:latest
    environment:
      POSTGRES_DB: your_db_name
      POSTGRES_USER: your_db_username
      POSTGRES_PASSWORD: your_db_password
    ports:
      - "5432:5432"
  webapp:
    build:
      context: .
      dockerfile: Dockerfile
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/your_db_name
      SPRING_DATASOURCE_USERNAME: your_db_username
      SPRING_DATASOURCE_PASSWORD: your_db_password
    ports:
      - "8080:8080"
    depends_on:
      - db
```

### 2. Build and Run with Docker Compose

Use Docker Compose to start the application and the PostgreSQL database:

```sh
docker-compose up --build
```

### 3. Access the Application

Once the services are up and running, you can access the application at `http://localhost:8080`.

### 4. Stopping the Services

To stop the services, run:

```sh
docker-compose down
```

## CI Pipeline

This project includes a GitHub Actions workflow for Continuous Integration (CI). The workflow is defined in `.github/workflows/ci.yml` and includes steps for building, testing, and running integration tests.

## Additional Notes

- Ensure that the database is created before running the application if not using Docker.
- Flyway will handle the database migrations automatically when the application starts.

## Troubleshooting

If you encounter any issues, please check the logs for more details:

```sh
mvn spring-boot:run
```

Feel free to open an issue in the repository if you need further assistance.
```

This 

README.md

 file provides clear instructions for running the application using Maven, with Docker as an optional step. It also includes information about the CI pipeline and troubleshooting tips.




