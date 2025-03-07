# Hospital Service API

## Overview

This is the backend service for managing hospital patient transfers. It provides RESTful APIs for handling patient records and transfer requests.

## Authors

- [@p-chk](https://www.github.com/p-chk) - Chayakorn K.

## Tech Stack

### **Server:**

```
- Java 11
- Spring Boot 2.6.6
- Gradle 7.4.1
```

### **Database:**

```
- PostgreSQL 13.1 Alpine
```

## **Run Locally**

To run the backend service locally, install necessary components before running the commands below:

```sh
./gradlew clean build

docker-compose up
```

## **API Endpoints**

### **Patient API** (`/api/patients`)

| Method | Endpoint                        | Description                |
| ------ | ------------------------------- | -------------------------- |
| `GET`  | `/api/patients`                 | Retrieve all patients      |
| `GET`  | `/api/patients/{id}`            | Get a patient by ID        |
| `GET`  | `/api/patients/phn/{phn}`       | Get a patient by PHN       |
| `POST` | `/api/patients`                 | Add a new patient          |
| `PUT`  | `/api/patients/{id}`            | Update an existing patient |
| `GET`  | `/api/patients/status/{status}` | Get patients by status     |

### **Transfer API** (`/api/outgoing-transfers`)

| Method | Endpoint                                    | Description                       |
| ------ | ------------------------------------------- | --------------------------------- |
| `POST` | `/api/outgoing-transfers/request-transfer`  | Request a patient transfer        |
| `GET`  | `/api/outgoing-transfers/patient/{id}`      | Get latest transfer for a patient |
| `POST` | `/api/outgoing-transfers/complete-transfer` | Complete a patient transfer       |



