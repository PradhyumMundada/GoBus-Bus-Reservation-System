# Viva Questions and Short Answers

1. **What is JDBC?** JDBC is the Java API used to connect Java programs to databases and run SQL.
2. **Why MySQL?** It is popular, free to learn, reliable, and works well with JDBC.
3. **Why Java Swing?** Swing is included with Java and is suitable for a simple desktop GUI.
4. **What is DAO?** A Data Access Object keeps database code separate from GUI code.
5. **What is PreparedStatement?** It is a parameterized SQL statement that safely sends values to the database.
6. **What is ResultSet?** It contains rows returned by a SQL query.
7. **What is a transaction?** It is a group of database steps treated as one unit.
8. **Why commit and rollback?** Commit saves all successful steps; rollback undoes partial work if a step fails.
9. **How is fare calculated?** Fare per seat multiplied by the number of booked seats.
10. **How are seats updated?** After inserting a booking, the booked count is subtracted from `available_seats`.
11. **How does cancellation restore seats?** It reads the booking's seat count and adds it back before committing.
12. **How is the booking ID generated?** MySQL creates it with `AUTO_INCREMENT`; JDBC reads the generated key.
13. **Why AUTO_INCREMENT?** It gives every new booking a unique numeric ID automatically.
14. **What is a foreign key?** It links one table to a valid row in another table; here `bookings.bus_id` links to `buses.bus_id`.
15. **Why use a JOIN?** A JOIN combines booking data with its bus name and route for display.
16. **What is encapsulation?** It keeps data private and exposes controlled access through methods.
17. **Where is encapsulation used?** `Bus` has private fields and public getter methods.
18. **What classes are present?** DatabaseConnection, Bus, BusDAO, BookingDAO, Main, and MainFrame.
19. **Role of Bus.java?** It represents one bus record in Java.
20. **Role of BusDAO.java?** It reads all buses and searches buses by route.
21. **Role of BookingDAO.java?** It handles booking, viewing, listing, and cancellation database operations.
22. **Role of MainFrame.java?** It is the Swing GUI the user operates.
23. **What happens when booking fails?** The transaction rolls back and no partial booking or seat update is saved.
24. **How is SQL injection prevented?** User values are passed through PreparedStatement parameters, not joined into SQL text.
25. **What if seats are insufficient?** The booking is rejected before any booking record or seat update is committed.
