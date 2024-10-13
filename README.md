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


## Docker Setup (Optional)