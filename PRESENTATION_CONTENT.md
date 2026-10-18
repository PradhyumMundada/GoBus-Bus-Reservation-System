# PBL Presentation Content (10 Slides)

1. **Title** — Bus Reservation System Using Java and MySQL; Java Swing + JDBC desktop application.
2. **Problem Statement** — Manual booking is slow and seat counts can become inaccurate. A computerized local system is needed.
3. **Objectives** — Search buses, book seats, calculate fare, generate booking IDs, view/cancel tickets, and maintain availability.
4. **Existing vs Proposed** — Existing: paper/manual, slow updates, error-prone. Proposed: searchable records, instant calculation, transactional updates.
5. **Technology Stack** — Java 25, Swing, JDBC, MySQL, Maven, IntelliJ IDEA, MySQL Connector/J.
6. **System Flow** — User → Swing GUI → DAO classes → JDBC → MySQL; booking uses check seats → insert booking → update seats → commit.
7. **Database Design** — `buses` one-to-many `bookings`; `bookings.bus_id` is the foreign key.
8. **Key Features** — Route search, passenger validation, multi-seat booking, generated ID, booking lookup, cancellation, rollback safety.
9. **Results / Screenshots** — Show Home, Search Result, Book Success, View Booking, and All Bookings screens during demo.
10. **Conclusion and Future Scope** — Reliable basic reservation management; next steps: login, payment, seat map, reports, online deployment.
