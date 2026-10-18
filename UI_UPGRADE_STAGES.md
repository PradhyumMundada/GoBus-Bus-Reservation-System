# GoBus Demo UI Upgrade — Stage-by-Stage Guide

This project uses a **demo checkout only**. It must not collect real card numbers, UPI IDs, wallet credentials, or connect to a payment gateway.

## Stage 1 — Set the design direction

Use a modern dark travel-booking appearance: charcoal background, white text, red primary actions, rounded cards, and clear spacing. Keep the original Bus Reservation System functions unchanged.

**Prompt:**

> Restyle my Java Swing Bus Reservation System as a dark travel-booking demo. Use a charcoal background, dark surface cards, readable white text, muted grey secondary text, and red primary buttons. Do not copy RedBus branding or use external assets. Keep the project beginner-friendly and retain all existing functionality.

## Stage 2 — Add journey date selection

Add a Swing `JSpinner` using `SpinnerDateModel` to the booking route row. Display it as `dd MMM yyyy`; default it to today. Show the chosen date in the final confirmation message.

**Prompt:**

> In `MainFrame.java`, add a Date of Journey control to the Book Ticket screen using `JSpinner` and `SpinnerDateModel`. Format it as `dd MMM yyyy`, default to today, validate it as a demo UI control, and include the selected date in the booking confirmation. Do not change the existing database schema yet.

## Stage 3 — Add a demo payment selector

Create a modal `JDialog` after passenger validation. It should contain three radio-button choices: UPI, Credit/Debit Card, and GoBus Wallet. Show the total amount and journey date. State clearly that it is a demo payment and that no money is charged. Only create the booking after the user clicks the confirmation button.

**Prompt:**

> Add a demo-only payment dialog to my Java Swing booking flow. After the user enters valid passenger details and clicks Continue to payment, show a dark modal with UPI, Credit/Debit Card, and Wallet radio-button options, plus journey date and total fare. The dialog must never collect real payment details or contact a payment provider. When the user clicks Pay and confirm booking, call the existing BookingDAO booking method and show the chosen payment method in the success dialog.

## Stage 4 — Make search and booking flow clearer

Use From/To fields, a swap button, clear buttons, a selected-bus summary, and visible fare/seat feedback. Keep the database's internal bus ID hidden from users; show only the public bus number.

**Prompt:**

> Improve the Java Swing booking flow. Keep the database bus ID hidden, show only the Bus Number in customer-facing tables, add From/To fields with a swap button and Clear action, show selected bus name/timings, and recalculate total fare when seat count changes. Preserve prepared statements and transaction-safe BookingDAO logic.

## Stage 5 — Test the demo safely

1. Search `Pune` to `Mumbai`.
2. Select the displayed bus and choose a future journey date.
3. Enter passenger details and select one seat.
4. Choose each payment option once in separate tests.
5. Confirm that each booking gets a new ID and seat availability decreases.
6. Cancel test bookings and confirm availability is restored.

**Prompt:**

> Create a concise manual test checklist for the Java Swing Bus Reservation System covering route search, journey date selection, UPI/Card/Wallet demo selection, booking confirmation, booking lookup, cancellation, and seat restoration. Do not require a real payment gateway or real user credentials.
