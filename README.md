# Bus Reservation System using Java and MySQL

A beginner-friendly college PBL desktop project built with Java 25, Swing, JDBC, MySQL, and Maven. It supports bus search, multi-seat booking, booking lookup, cancellation, and live seat availability.

## Setup

1. Install JDK 25, MySQL Server, and Maven. Create a schema and sample data by running `database_setup.sql` in MySQL Workbench.
2. Open `src/main/java/com/busreservation/database/DatabaseConnection.java` and replace `YOUR_MYSQL_PASSWORD` with your local MySQL root password. Do not commit that password.
3. From this folder, run:

   ```powershell
   mvn clean compile
   mvn exec:java
   ```

   The main class in the Maven configuration is `MainFrame`, the Swing application. For the console test menu, run `java -cp target/classes com.busreservation.database.Main` after compiling.

## Project layout

- `DatabaseConnection.java` — MySQL JDBC connection
- `Bus.java` — encapsulated bus model
- `BusDAO.java` — read/search bus records
- `BookingDAO.java` — transactional booking, lookup, listing, and cancellation
- `Main.java` — optional console tester
- `MainFrame.java` — primary Swing interface
- `database_setup.sql` — tables plus four supplied buses
- `reset_demo_state.sql` — removes demonstration bookings and restores all seats

## Notes for demonstration

Use `Pune` and `Mumbai` to find Shivneri Express. Book a seat, note the generated booking ID, use it in View Booking, then cancel it. The cancellation transaction restores the booked seats.

The password placeholder is intentionally required before a database connection can succeed. The code is compiled against MySQL Connector/J 9.5.0 by Maven.
