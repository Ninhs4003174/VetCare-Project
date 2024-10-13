# RMIT COSC2299 SEPT Major Project

## Group Information

### Group-P04-03

### Members
- Harmandeep Singh (s4009171)
- Gurnoor Kaur (s3991487)
- Fazila Qurban Ali (s3667195)
- Krishitaa Purusothaman (s3962111)
- Mohamed Bilal Naeem (s3967700)
- Ninh Duy Huynh (s4003174)

## Project Overview

This project is part of the Software Engineering Process and Tools (COSC2299) course at RMIT. Our team developed a full-stack web application to manage a VetCare system. The project implements user authentication, appointment scheduling, medical record access, and other core features. The technologies used in this project include Spring Boot (Java), PostgreSQL for database management, and Docker for containerization.



## Setup and Installation Instructions

### Prerequisites
Before running the project, ensure that you have the following installed on your system:

- Java 17 or higher
- Maven for project build and dependency management
- PostgreSQL (preferably version 15 or higher)
- Docker (if using containerization)
- Git for version control

## Database Setup

1. **Install PostgreSQL:** Download and install PostgreSQL from the official [website.](https://www.postgresql.org/download/)
2. **Create Database:** Once PostgreSQL is installed, create a new database for the project:
```
psql -U <your-username>
CREATE DATABASE milestone2;
```
3. **Run Migrations:** The project uses Flyway for database migrations. When the application starts, Flyway will automatically run the migrations and set up the required tables.

## Project Configuration
1. **Clone the Repository:**
Clone the project from GitHub using the following command:
```
git clone https://github.com/cosc2299-2024/team-project-group-p04-03.git
cd team-project-group-p04-03

```

2. **Configure Database Credentials:** Open the src/main/resources/application.properties file and update it with your PostgreSQL username and password.

## Running the Project
1. **Build the Project:**
Build the project using Maven by running the following command:
```
./mvnw clean install

```


2. **Run the Project:**
Start the application using Maven:
Build the project using Maven by running the following command:
```
./mvnw spring-boot:run

```
3. **Access the Application:**
Open your browser and navigate to:]
```
http://localhost:8080

```

# Exploring the Website

### Client Dashboard
Once signed up as a client, log in with your newly created credentials.
As a client, you can:
- Schedule and manage appointments.
- View and access medical records for your pets.
- Browse through educational resources and tips. (Note: Resources will first be empty until admin approves them)
- Can view profile, edit profile and add/edit pets. 
- User can view prescriptions and then request prescriptions.(prescriptions must first be approved by the vet you have booked an appointment with.)



## Logging in as a Predifined User.
To explore different roles and dashboards, you can use the following predefined accounts, created during the project initialization:

#### Client Dashboard:
- Username: user

- Email: user@example.com

- Password: user123

After logging in, you will have access to the client dashboard, where you can manage appointments and view resources.


#### Receptionist (Clinic) Dashboard:
- Username: clinic
- Email: clinic@example.com
- Password: clinic123
After logging in, you will access the clinic dashboard, where you can manage appointments and view clinic-related information.

#### Vet Dashboard:
- Username: vetuser
- Email: vet@example.com
- Password: vet123
After logging in as a vet, you can:
Manage your schedule and view upcoming appointments.
Access medical records for pets under your care.

#### Admin Dashboard:
- Username: admin
- Email: admin@example.com
- Password: admin123
After logging in as an admin, you can:
Manage users (add or delete users).
Review and approve or deny educational resources.
Manage clinics and vet information.


**Note:** only when admin approves resources, only then will it show up on client dashbaord.

## Docker Setup
If you want to run the project using Docker:

1. **Install Docker:**
Download and install Docker from the official  [website.](https://www.docker.com/get-started/)


2. **Configure Docker Credentials:**  Open the docker-compose.yml file and update it with your postgreSQL details.
```
services:
  db:
    image: postgres:13
    container_name: postgres
    environment:
      POSTGRES_DB: milestone2
      POSTGRES_USER: <your-username>      # Replace with your PostgreSQL username
      POSTGRES_PASSWORD: <your-password>  # Replace with your PostgreSQL password

  webapp:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: springboot-app
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/milestone2
      SPRING_DATASOURCE_USERNAME: <your-username>      # Replace with your PostgreSQL username
      SPRING_DATASOURCE_PASSWORD: <your-password>      # Replace with your PostgreSQL password

```

3. **Build Docker Image:**
Use Docker to build the image for the application:
```
docker build -t vetcare-app .

```
4. **Run Docker Container:**
Start the Docker container:
```
docker-compose up --build

```
5. **Access the Application:**
Visit the following URL in your browser:

```
http://localhost:8080

```