# Help Desk / Support Ticket Management System
The application allows users to raise support tickets, support agents to work on tickets, and admins to manage users and view all tickets.

## Project Description
The application contains three roles.
### Admin
Admin can perform:
- Create users
- Create support agents
- View all users
- View all tickets
- Assign tickets to agents
- Change user active status

### Support Agent
Support Agent can perform:
- View tickets assigned to them
- Update ticket status
- Add comments to tickets
- Resolve tickets

### Employee
- Login
- Create support tickets
- View their own tickets
- Add comments to their tickets
- Close their resolved ticket

## Tech Stack & Dependencies
- **Language:** Java 21
- **Framework:** Spring Boot 4.1.1 (Spring Web, Spring Data JPA, Spring Security, Validation, Oauth2-Resource-Server, Postgres JDBC Driver)
- **Database:** PostgreSQL 17
- **Tools** Lombok, Swagger UI
- **Build Tool:** Gradle v9.4

## Configuration
The application relies on configurations defined in `src/main/resources/application.properties`.

Create a local database
```
spring.datasource.url=jdbc:postgresql://localhost:5432/{your_db_name}
spring.datasource.username={your_db_username}
spring.datasource.password={your_db_password}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

jwt.secret={your_jwt_secret}
jwt.issuer={your_jwt_issuer}
jwt.expiry={jwt_exipiry time in seconds}

encryption.key={your_encryption_key}
encryption.cipher={your_encryption_alogrithm}
```

## Build and Run
1. **Clone the repository:**
```
bash
   git clone https://github.com
   cd Pavanteja-21/support-ticket-management-system
```

2. **Build the application:**
   Using Gradle:
   ```bash
   ./gradlew build
   ```
3. **Run the application:**
   ```
   Using Gradle:
   ```bash
   ./gradlew bootRun
   ```

The server will start locally at `http://localhost:8080`.

## API Endpoints & Usage
Once the application is running, you can interact with the following core endpoints:

| HTTP Method | Endpoint | Description | Auth Required | Role |

### Auth API End Points
- | `POST` | `/api/auth/register` | Only Admin can able to register a new user | **Yes** (Bearer Token) | ADMIN |
- | `POST` | `/api/auth/login` | Login to receive a JWT Token | No | No |
- | `POST` | `/api/auth/reset/password` | For Changing the existing password | **Yes** (Bearer Token) | No |


### Admin API End Points
- | `POST` | `/api/admin/role` | Only Admin can add roles | **Yes** (Bearer Token) | ADMIN |
- | `GET` | `/api/admin/employees` | View  all employee role | **Yes** (Bearer Token) | ADMIN |
- | `GET` | `/api/admin/agents` | View  all the agents| **Yes** (Bearer Token) | ADMIN |
- | `GET` | `/api/admin/tickets` | Only Admin can view all created tickets | **Yes** (Bearer Token) | ADMIN |
- | `PATCH` | `/api/admin/ticket/{ticketId}/assign/{agentId}` | Only Admin can assign the ticket to a agent | **Yes** (Bearer Token) | ADMIN |
- | `GET` | `/api/admin/ticket/summary` | Admin can view the ticket summary | **Yes** (Bearer Token) | ADMIN |

### Support Agent API End Points
- | `GET` | `/api/agent/tickets` | Agent can view all his assigned tickets by Admin | **Yes** (Bearer Token) | SUPPORT_AGENT |
- | `PATCH` | `/api/agent/ticket/update/status` | Agent can update the status of tickets that are assigned to him | **Yes** (Bearer Token) | SUPPORT_AGENT |
- | `POST` | `/api/agent/{ticketId}/add/comment` |  Agent can add comment in ticket | **Yes** (Bearer Token) | SUPPORT_AGENT |

### Employee API End Points
- | `POST` | `/api/employee/add/ticket` | Only Employee can create the ticket | **Yes** (Bearer Token) | EMPLOYEE |
- | `GET` | `/api/employee/tickets` | Only Employee can view their created tickets |  **Yes** (Bearer Token) | EMPLOYEE |
- | `POST` | `/api/employee/{ticketId}/add/comment` | Employee can add comment to his ticket"| **Yes** (Bearer Token) | EMPLOYEE |
- | `PATCH` | `/api/employee/{ticketId}/close` | Employee can close their resolved tickets | **Yes** (Bearer Token) | EMPLOYEE |


