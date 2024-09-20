# Wallet Backend

## Project Overview

The Wallet Backend is a basic bookkeeping (accounting) service that keeps track of monetary transactions for accounts, similar to a bank account. It allows you to create accounts, check balances, transfer funds, and view transaction history via a REST API.

## Features

- **Get Balance**: Fetch the balance for a specific account.
- **Transfer Funds**: Transfer funds between accounts or add/remove funds from an account.
- **List Transactions**: List all transactions for an account.
- **Create Account (Optional)**: Accounts can be created explicitly or implicitly when performing transactions.

## Technologies Used

- **Java 17+**
- **Spring Boot** for RESTful API
- **H2 Database** (in-memory database for quick testing)
- **Gradle** for build automation
- **JPA/Hibernate** for ORM (Object-Relational Mapping)

## Getting Started

### Prerequisites

Ensure you have the following installed:

- [Java 17+](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Gradle](https://gradle.org/install/)
- [Git](https://git-scm.com/)

### Setting Up the Project

**1. **Clone the repository**:**

   ```bash
   git clone https://github.com/JATPresentacion97/Wallet_Backend.git
   cd Wallet_Backend
   
**2. Build the project:**

Run the following Gradle command to build the project:

bash

./gradlew build

**3. Run the application:**

You can run the Spring Boot application using the following command:

bash

./gradlew bootRun

The server will start on http://localhost:8080.

**API Endpoints**

**1. Get Balance**
URL: /api/v1/wallet/{accountId}/balance
Method: GET
Description: Fetch the balance for a specific account.

**3. Transfer Funds**
URL: /api/v1/wallet/transfer
Method: POST
Description: Transfer funds to/from an account.
Request Body:
json

{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 100.00
}

**4. List Transactions**
URL: /api/v1/wallet/{accountId}/transactions
Method: GET
Description: List all transactions for a specific account.

**5. Create Account (Optional)**
URL: /api/v1/wallet/create
Method: POST
Description: Create a new account.
Request Body:
json

{
  "initialBalance": 500.00
}
