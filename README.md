# 🚌 GoBus – Bus Reservation System

A Java-based desktop application for managing bus searches and ticket reservations. GoBus provides a graphical user interface built with **Java Swing**, with **MySQL** for persistent data storage and **JDBC** for database connectivity.

Developed as a college project to demonstrate object-oriented programming, SQL, database integration, and reservation management.

## ✨ Features

- **Bus Search:** Search buses by source and destination.
- **Seat Booking:** Book one or multiple seats in a single reservation.
- **Automatic Fare Calculation:** Calculate the total fare based on the selected bus and number of seats.
- **Booking Lookup:** Retrieve reservation details using a booking ID.
- **Booking Cancellation:** Cancel a reservation and restore seat availability.
- **Database Integration:** Store bus and booking records in MySQL.
- **Desktop Interface:** Interact with the system through a Java Swing GUI.
- **Database Transactions:** Manage booking-related database updates consistently.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 25 | Application logic and object-oriented programming |
| Java Swing | Desktop graphical user interface |
| JDBC | Java-to-MySQL connectivity |
| MySQL | Relational database and persistent storage |
| SQL | Database queries and updates |
| Apache Maven | Build and dependency management |
| MySQL Connector/J 9.5.0 | JDBC driver for MySQL |

## 📋 Prerequisites

Install the following before running the application:

- JDK 25
- MySQL Server and MySQL Workbench
- Apache Maven
- Git (optional, for cloning the repository)

Verify your installations:

```bash
java -version
mvn -version
```

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/PradhyumMundada/GoBus-Bus-Reservation-System.git
cd GoBus-Bus-Reservation-System
```

Alternatively, download the repository as a ZIP file from GitHub and extract it.

### 2. Set up the database

1. Start MySQL Server.
2. Open MySQL Workbench and connect to your local server.
3. Open `database_setup.sql`.
4. Execute the script to create the required database tables and insert the supplied sample bus records, as defined in the SQL file.

Make sure the database and table names match those configured in the Java application.

**Note:** Review the SQL script before running it, particularly if you already have a database containing records you want to preserve.

### 3. Configure the database connection

Open:

`src/main/java/com/busreservation/database/DatabaseConnection.java`

Configure the following connection settings for your local MySQL installation:

- Database URL
- MySQL username
- MySQL password

For security, use an environment variable for the password rather than committing a real password to the source code.

For example, if your application reads the `GOBUS_DB_PASSWORD` environment variable, set it in Windows Command Prompt:

```cmd
set GOBUS_DB_PASSWORD=YOUR_LOCAL_MYSQL_PASSWORD
```

Replace the placeholder locally. Never publish your actual password.

The environment variable must be set in the same Command Prompt session from which you launch the application.

### 4. Compile the project

From the project root directory, run:

```bash
mvn compile
```

### 5. Launch the application

```bash
mvn exec:java
```

The main application window should open if the Maven configuration, database connection, and database setup are correct.

**Optional:** To run the console-based test menu after compiling:

```bash
java -cp target/classes com.busreservation.database.Main
```

## 📁 Project Structure

```text
GoBus-Bus-Reservation-System/
├── src/
│   └── main/
│       └── java/
│           └── com/busreservation/database/
│               ├── Main.java
│               ├── MainFrame.java
│               ├── DatabaseConnection.java
│               ├── Bus.java
│               ├── BusDAO.java
│               └── BookingDAO.java
├── database_setup.sql
├── reset_demo_state.sql
├── pom.xml
├── README.md
├── PROJECT_DOCUMENTATION.md
├── PRESENTATION_CONTENT.md
├── UI_UPGRADE_STAGES.md
├── VIVA_QA.md
└── .gitignore
```

## ⚙️ How It Works

1. The user searches for buses using a source and destination.
2. The application displays matching buses and their details.
3. The user selects a bus, enters passenger details, and chooses the number of seats.
4. The application calculates the total fare and saves the reservation.
5. The database updates the available seat count after a successful booking.
6. The user can retrieve a reservation using its booking ID.
7. On cancellation, the application updates the booking data and restores the relevant seats.

### Application Architecture

```text
User
  ↓
Java Swing Interface
  ↓
Application Logic and DAO Classes
  ↓
JDBC
  ↓
MySQL Database
```

## 🧪 Demo Instructions

For a quick demonstration:

1. Launch GoBus.
2. Search for buses from **Pune** to **Mumbai**.
3. Select a matching bus, such as Shivneri Express if present in your sample data.
4. Book one or more seats.
5. Note the generated booking ID.
6. Use the booking lookup feature to retrieve the reservation.
7. Cancel the booking and verify that the seat availability is restored.

The available routes depend on the records in your local database.

## 🔒 Security Notes

- Do not commit real database passwords, API keys, or other credentials.
- Keep local configuration and environment files out of version control.
- The `.gitignore` file helps exclude specified local and generated files.
- If a password was previously pushed to a public repository, change it and remove it from Git history as appropriate.
- Use a dedicated database account with limited permissions instead of a root account for a production deployment.

## 🔮 Future Enhancements

- User registration and authentication
- Administrator dashboard for managing buses
- Printable tickets and booking receipts
- Booking history and reports
- Improved validation and error handling
- Web or mobile application interface
- Integration with a secure payment gateway

## 👨‍💻 Authors

- **Pradhyum Mundada**
- **Dhruti Jagtap**

## 📄 License

No license has been specified yet. If you want others to reuse or modify this project, consider adding an appropriate open-source license.

---

**Project:** GoBus – Bus Reservation System  
**Built with:** Java Swing, JDBC, MySQL, and Maven
