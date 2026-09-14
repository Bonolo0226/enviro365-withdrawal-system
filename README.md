# Enviro365 Investments – Withdrawal Notice System

A full-stack system that allows investors to view their portfolio, submit withdrawal requests, and download withdrawal statements as CSV. Built as part of the eTalente Junior Developer Assessment (2026).

## Tech Stack
- **Backend:** Java 21, Spring Boot 4.1.1, Spring Data JPA, H2 (in-memory database)
- **Frontend:** Plain HTML, CSS, JavaScript (fetch API)
- **Build tool:** Maven

## Features
- View investor portfolio (details + products)
- Submit withdrawal notices with automatic balance calculation
- Export withdrawal statements as CSV, with optional date filtering
- Business rule validation:
  - Retirement product withdrawals only allowed for investors older than 65
  - Withdrawal amount cannot exceed the product's balance
  - Withdrawal amount cannot exceed 90% of the product's balance
- Advanced features implemented:
  - **DTO layer** – separates API request/response shapes from database entities
  - **Global exception handling** – consistent JSON error responses across the API
  - **Input validation** – rejects malformed requests (e.g. negative amounts) before business logic runs

## Getting Started

### Prerequisites
- Java 21 (or later)
- No separate database installation needed (H2 runs in-memory)

### Running the app
```bash
./mvnw spring-boot:run
```
(On Windows: `.\mvnw.cmd spring-boot:run`)

The app will start on **http://localhost:8080**. Sample data (2 investors, 4 products) is seeded automatically on startup.

### Using the app
Open **http://localhost:8080/index.html** in your browser.

### Accessing the H2 database console
Visit **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:enviro365db`
- Username: `sa`
- Password: *(leave blank)*

## API Documentation

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/portfolio/{investorId}` | Get an investor's portfolio (details + products) |
| POST | `/api/withdrawals` | Submit a withdrawal request. Body: `{ "productId": number, "amount": number }` |
| GET | `/api/withdrawals/product/{productId}` | Get withdrawal history for a product |
| GET | `/api/withdrawals/export/{investorId}?from={date}&to={date}` | Download a CSV withdrawal statement (date filters optional, format `YYYY-MM-DD`) |

### Sample seeded data
| Investor | Age | Product | Type | Balance |
|---|---|---|---|---|
| Thabo Mokoena (id 1) | 70 | Golden Years Retirement Fund | RETIREMENT | 100,000 |
| Thabo Mokoena (id 1) | 70 | Flexible Savings | SAVINGS | 50,000 |
| Naledi Dube (id 2) | 40 | Unit Trust Growth Plan | UNIT_TRUST | 30,000 |
| Naledi Dube (id 2) | 40 | Early Retirement Fund | RETIREMENT | 20,000 |

## AI Usage Disclosure

AI assistance (Claude) was used throughout this project as a **guided pair-programming tool** — each file was explained, written, then manually run and verified by me before proceeding to the next. I can walk through and explain any part of this codebase, including:
- Why entities, DTOs, and services are separated into layers
- How Spring Data JPA generates queries from repository method names
- How dependency injection wires the Controller → Service → Repository chain together
- How the global exception handler converts business rule violations into clean API responses
- The design trade-off of using H2 in-memory storage (fast to set up, but data resets on restart)

## Screenshots

<img width="1600" height="900" alt="successful_withdrawal" src="https://github.com/user-attachments/assets/fb816efd-91cd-42a8-bb0c-9b4ad2a22685" />
<img width="1600" height="900" alt="rejected_withdrawal" src="https://github.com/user-attachments/assets/0fe4eab9-4296-4d8c-9d18-4293a49889a3" />
<img width="1600" height="900" alt="90%_withdrawal_rule" src="https://github.com/user-attachments/assets/c07054f3-53be-44aa-9ad6-a35d493b534b" />
<img width="1600" height="900" alt="withdrawal_history_table" src="https://github.com/user-attachments/assets/58d3416d-b5da-47e7-a7c2-7f23a7e1ab56" />
<img width="1600" height="900" alt="withdrawal_statement" src="https://github.com/user-attachments/assets/c907aabb-03af-43a7-91b7-3359df91f1d3" />
<img width="1600" height="857" alt="H2_investors_table" src="https://github.com/user-attachments/assets/f0c7794d-09c0-4de3-9ba0-9b7ab9b255ef" />
<img width="1600" height="860" alt="H2_products_table" src="https://github.com/user-attachments/assets/6eef0d17-5932-4765-b9b5-6c77c255e07b" />
<img width="1600" height="900" alt="H2_withdrawal_notices" src="https://github.com/user-attachments/assets/23f48383-a601-41c3-ae71-41105f7f2ff1" />






