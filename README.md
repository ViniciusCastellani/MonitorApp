# README

## Android CRUD App for Monitor Management

### Overview

This project is an Android application that performs CRUD operations for managing monitors. It communicates with a Spring Boot backend via HTTP using Volley, with the backend storing data in an **H2 file-based database** accessed through JDBC.

### Features

- **Create**: Add new monitors with attributes like name, type, size, and price.
- **Read**: Retrieve all monitors or specific monitor details by ID.
- **Update**: Modify monitor information.
- **Delete**: Remove monitors from the database.
- **Backend Communication**: HTTP requests are managed using Volley.
- **Database**: File-based H2 database.

---

### Backend Configuration

#### 1. **application.properties**
The backend uses a file-based H2 database with JDBC, and the configuration is as follows:
```properties
spring.application.name=monitor-service

# Server Port
server.port=8080

# H2 Database Configuration
spring.datasource.url=jdbc:h2:file:./monitor-service
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
```

The database file will be stored in the project directory with the name `monitor-service`.

#### 2. **Database Table**
The database table `MONITOR` is created with the following schema:
```sql
CREATE TABLE MONITOR (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nome    VARCHAR(100) NOT NULL,
    tipo    VARCHAR(100) NOT NULL,
    tamanho DOUBLE NOT NULL,
    preco   DOUBLE NOT NULL
);
```

#### 3. **MonitorController**
The backend's REST controller handles CRUD operations under `/api/monitor`:

| Method   | Endpoint          | Description                   |
|----------|-------------------|-------------------------------|
| `GET`    | `/api/monitor`    | Retrieve all monitors         |
| `GET`    | `/api/monitor/{id}`| Retrieve a monitor by ID      |
| `POST`   | `/api/monitor`    | Add a new monitor             |
| `PUT`    | `/api/monitor`    | Update an existing monitor    |
| `DELETE` | `/api/monitor/{id}`| Delete a monitor by ID        |

---

### Android App Configuration

#### 1. **ComunicacaoServer**
The `ComunicacaoServer` class manages all HTTP requests to the backend using Volley. The base URL is dynamically constructed using the server IP:
```java
private String ipServer = "";  // Set the server IP address
private String url = "http://" + ipServer + ":8080/api/monitor";
```

This class contains methods for:
- **GET**: Fetching all monitors or a specific monitor.
- **POST**: Adding a new monitor.
- **PUT**: Updating a monitor.
- **DELETE**: Deleting a monitor.

#### 2. **Monitor Class**
The Android app uses a local `Monitor` class for managing monitor data, matching the backend model:
```java
public class Monitor {
    private int id;
    private String nome;
    private String tipo;
    private double tamanho;
    private double preco;

    // Getters and setters
}
```

---

### Setup Instructions

#### Backend Setup
1. Clone or download the Spring Boot backend project.
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the H2 console at `http://localhost:8080/h2-console`:
   - **JDBC URL**: `jdbc:h2:file:./monitor-service`
   - **Username**: `sa`
   - **Password**: `password`

#### Android App Setup
1. Clone or download the Android project.
2. Open the project in Android Studio.
3. Update the `ipServer` variable in the `ComunicacaoServer` class with your server's IP address.
4. Build and run the app on an emulator or physical device.

---

### Example JSON Payloads

#### Create a Monitor (`POST /api/monitor`)
```json
{
  "nome": "Monitor Samsung",
  "tipo": "LED",
  "tamanho": 24.5,
  "preco": 199.99
}
```

#### Update a Monitor (`PUT /api/monitor`)
```json
{
  "id": 1,
  "nome": "Monitor LG UltraWide",
  "tipo": "IPS",
  "tamanho": 34.0,
  "preco": 499.99
}
```

---

### Future Improvements
- Enhance error handling for failed HTTP requests on the Android app.
- Add input validation for monitor details in both the app and backend.
- Implement pagination for fetching monitor data.
- Provide a more user-friendly interface for monitor management.

---

### Contributions
Contributions are welcome! Fork the repository, make changes, and submit a pull request.
