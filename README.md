# Billing Application

## Overview

The **Billing App** calculates the final bill amount for a user after applying various discounts based on their user
type (Employee, Affiliate, or Customer) and discount on bill amount. Additionally, the service supports real time
currency conversion for bills by integrating with an external exchange rate API.

## Key Features

- Dynamic discount calculation based on user type using the **Strategy Design Pattern**.
- Additional discounts on bills over certain amounts.
- Realtime currency conversion for the final bill amount using an external API.
- Easily extendable architecture to add new user type, discount strategies or currency conversion logic.

## High-Level Design Details

1. **Billing Controller & Service**:
    - `BillingController` handles billing API request.
    - `BillingService` does the overall billing process, including discounts and currency conversion.

2. **Discount Strategy Factory**:
    - Implements the Strategy Design Pattern to dynamically choose the correct discount strategy based on the user type.
    - User types supported: `EMPLOYEE`, `AFFILIATE`, and `CUSTOMER`.
    - Using Factory and Strategy design pattern enables scalability of new user type and discount rules.

3. **Discount Strategies**:
    - **EmployeeDiscount**: 30% discount on non-grocery items.
    - **AffiliateDiscount**: 10% discount on non-grocery items.
    - **CustomerLoyaltyDiscount**: 5% discount on non-grocery items for customers associated for more than 2 years.

4. **Currency Exchange Rate Service**:
    - Integrates with an [ExchangeRate-API](https://www.exchangerate-api.com/) to fetch currency conversion rates.
    - Used [Pair API](https://www.exchangerate-api.com/docs/pair-conversion-requests) to get conversion rate between two
      currencies.

5. **Resilient and Modular Design**:
    - Ensures separation of concerns between billing logic, discount calculation, and currency conversion.
    - Log information for debugging and monitoring purposes.

6. **Authentication & Authorization**:
    - Just for this demo application implemented ImMemoryUserDetail providing three users `USER`, `ADMIN`, and `GUEST`.
    - API is accessible only for `USER`, `ADMIN` users only.
    - `GUEST` user will get 403 Unauthorised error (Added to demonstrate authorization).

## Design Details

### Design Patterns Used

1. **Strategy Pattern**:
    - Used for selecting the discount calculation logic dynamically based on the user type.

2. **Factory Pattern**:
    - Encapsulates the creation logic of discount strategies in `DiscountStrategyFactory`.

3. **Separation of Concerns**:
    - The project cleanly separates concerns across services and components to ensure maintainability and testability.

## Technologies Used

- **Java 17**
- **Spring Boot 3.0**
- **Spring Security** for authentication and authorization
- **Lombok** for reducing boilerplate code
- **JUnit 5** and **Mockito** for testing
- **SLF4J** for logging

## Prerequisites

- **Java 17** or later
- An IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code)
- Postman or cURL for testing APIs

## How to Run the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/avidee007/billing-app
   cd billing-app
   ```
2. Update application properties:
    - Configure the following property in the `src/main/resources/application.properties` file:
      ```properties
      app.currency.exchange.api.key=<API_KEY>
      ```
3. Build the project:
   ```bash
   ./gradlew build
   ```
4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application will be available at `http://localhost:8080/billing-service`.

## API Endpoints

### 1. Calculate Final Bill

**Endpoint**: `POST /api/v1/calculate`

**Request Body**:

```json
{
  "userType": "EMPLOYEE",
  "tenure": 4,
  "totalAmount": 700,
  "originalCurrency": "AED",
  "targetCurrency": "INR",
  "items": [
    {
      "category": "OTHER",
      "price": 500
    },
    {
      "category": "GROCERIES",
      "price": 200
    }
  ]
}
```

**Response**:

```json
{
  "finalAmount": 12104.251,
  "currency": "INR"
}
```

## Testing the API

### 1. Using Postman

- Import the [Postman collection](postman/BillingServiceAPI.postman_collection.json) into Postman.
- Send the request to the `/api/v1/calculate` endpoint.

### 2. Using cURL

```bash
curl -X POST http://localhost:8080/billing-service/api/v1/calculate \
-H "Content-Type: application/json" \
-H "Authorization: Basic dXNlcjpwYXNzd29yZA=="
-d '{
  "userType": "EMPLOYEE",
  "tenure": 1,
  "totalAmount": 700,
  "originalCurrency": "AED",
  "targetCurrency": "INR",
  "items": [
    {
      "category": "OTHER",
      "price": 500
    },
    {
      "category": "GROCERIES",
      "price": 200
    },
  ]
}'
```

### 3. Unit Tests

- Before running tests update api key else one integration test will fail.
- Run unit tests using Gradlew:
  ```bash
  ./gradlew test
  ```

## UML Class Diagram

UML class diagram and plantUml code for UML diagram can be found in [here](uml).
![uml.png](uml/UMLClassDiagram.png)

## Code Coverage & Sonar report

### Code Coverage report

![code-coverage.png](images/code.png)

### Sonar Report

![code-coverage.png](images/sonar.png)
  

