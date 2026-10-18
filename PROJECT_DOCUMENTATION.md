# Bus Reservation System Using Java and MySQL

## 1. Title and abstract

**Bus Reservation System Using Java and MySQL** is a desktop application for booking and managing bus tickets. It lets an operator search buses, record passenger details, book one or more seats, calculate fares, view bookings, and cancel bookings. Java Swing provides the interface, JDBC connects the program to MySQL, and transactions keep bookings and available-seat counts consistent.

## 2. Problem statement, objectives, motivation, and scope

Manual ticket registers are slow and can lead to duplicate or incorrect seat counts. The project digitizes the basic reservation workflow. Its objectives are to show buses, search routes, book/cancel tickets, maintain seat availability, and retain booking records. It is motivated by the need for a simple, reliable local system suitable for small operators and college learning. The scope is a single desktop user connected to a local MySQL database; payment processing, user login, and live GPS tracking are outside scope.

## 3. Technologies and requirements

Java 25, Java Swing, JDBC, MySQL Server, MySQL Connector/J 9.5.0, Maven, and IntelliJ IDEA are used. The computer needs JDK 25, Maven, MySQL running on port 3306, and a display capable of running Swing. The application expects database `bus_reservation` and the local root password configured only in `DatabaseConnection.java`.

## 4. Functional and non-functional requirements

Functional requirements: list buses; search source/destination; select a bus; book seats; calculate total fare; create a booking ID; view one/all bookings; cancel a booking; restore seats; and exit after confirmation. Non-functional requirements: a simple readable GUI, input validation, prepared SQL statements, transaction safety, responsive local use, and maintainable beginner-friendly classes.

## 5. Architecture and modules

```
Swing GUI / Console
        |
        v
BusDAO + BookingDAO  (business/database operations)
        |
        v
DatabaseConnection (JDBC)
        |
        v
MySQL: buses, bookings
```

`Bus` is the model. `BusDAO` reads and searches buses. `BookingDAO` inserts, finds, lists, and cancels bookings. `DatabaseConnection` centralizes JDBC setup. `MainFrame` is the primary GUI and `Main` is an optional console backend test menu.

## 6. Database design and ER description

`buses` has `bus_id` (primary key), bus number/name, route, times, total and available seats, and fare. `bookings` has `booking_id` (primary key), `bus_id` (foreign key), passenger details, seat count, fare, and timestamp. One bus can have many bookings; every booking belongs to one bus.

| Table | Key fields | Purpose |
|---|---|---|
| buses | `bus_id` PK, `bus_number` UNIQUE | Master bus and availability data |
| bookings | `booking_id` PK, `bus_id` FK | Passenger reservations |

## 7. Flow and algorithm

Search: take source/destination → validate → query with `PreparedStatement` → show table. Booking: select bus → enter passenger details and seat count → read/lock the bus row → verify availability → insert booking → reduce available seats → commit. On any SQL failure, roll back. Cancellation: find/lock booking → delete it → restore the exact booked seats → commit; otherwise roll back.

## 8. Implementation details

All user-provided database values use `PreparedStatement`, preventing SQL injection. Booking IDs come from MySQL `AUTO_INCREMENT`, returned through `Statement.RETURN_GENERATED_KEYS`. The `FOR UPDATE` queries lock the relevant row during the transaction to help avoid concurrent overbooking. Swing uses `CardLayout` for navigation, `JTable` for tabular results, spinners for constrained age/seats, and dialogs for validation feedback.

## 9. Testing and results

| Test | Expected result |
|---|---|
| View All Buses | Four supplied buses are displayed |
| Pune → Mumbai | Shivneri Express appears |
| Pune → Nashik | City Express appears |
| Mumbai → Delhi | Shivneri Travels appears |
| Book 1 Shivneri seat | ₹450 booking; seats 40 → 39; generated ID |
| View generated ID | Bus and passenger details appear |
| View all bookings | New record appears |
| Cancel generated ID | Record deleted; seats 39 → 40 |
| Invalid booking ID | Clear message, no crash |
| More seats than available | Booking rejected |

Run `reset_demo_state.sql` after a test demonstration to remove bookings and set each bus's available seats to its total.

## 10. Advantages, limitations, future scope, conclusion

Advantages include faster lookup, automatic fare calculation, consistent seat counts, and simple database-backed records. Limitations are local single-user use, no login/payment, and no seat-number selection. Future scope includes authentication, seat maps, online payments, reports, notifications, and multi-user deployment. The project demonstrates core Java OOP, Swing, JDBC, SQL joins, prepared statements, and transactions in an understandable reservation workflow.
