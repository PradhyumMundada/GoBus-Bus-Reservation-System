package com.busreservation.database;

import java.util.ArrayList;
import java.util.Scanner;

/** Optional console menu for testing the same JDBC backend without the GUI. */
public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final BusDAO busDAO = new BusDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    public static void main(String[] args) { new Main().run(); }

    private void run() {
        while (true) {
            System.out.println("\n--- BUS RESERVATION SYSTEM ---\n1. Search Bus\n2. Book Ticket\n3. View Booking\n4. Cancel Booking\n5. View All Buses\n6. View All Bookings\n7. Exit");
            switch (readInteger("Choose an option: ")) {
                case 1 -> searchBus(); case 2 -> bookTicket(); case 3 -> System.out.println(bookingDAO.viewBooking(readPositiveInteger("Booking ID: ")));
                case 4 -> System.out.println(bookingDAO.cancelBooking(readPositiveInteger("Booking ID: ")) ? "Booking cancelled." : "Cancellation failed. Check the ID.");
                case 5 -> printBuses(busDAO.getAllBuses()); case 6 -> bookingDAO.viewAllBookings(); case 7 -> { System.out.println("Goodbye."); return; }
                default -> System.out.println("Please choose 1 to 7.");
            }
        }
    }

    private void searchBus() { printBuses(busDAO.searchBuses(readNonEmptyString("Source: "), readNonEmptyString("Destination: "))); }
    private void bookTicket() {
        int busId = readPositiveInteger("Bus ID: ");
        boolean success = bookingDAO.bookTicket(busId, readNonEmptyString("Passenger name: "), readAge(), readGender(), readPositiveInteger("Seats: "));
        System.out.println(success ? "Booking successful. Booking ID: " + bookingDAO.getLastBookingId() : "Booking failed. Check the bus and available seats.");
    }
    private void printBuses(ArrayList<Bus> buses) { if (buses.isEmpty()) System.out.println("No buses found."); else buses.forEach(bus -> System.out.println(bus.display())); }
    private int readInteger(String prompt) { while (true) try { System.out.print(prompt); return Integer.parseInt(scanner.nextLine().trim()); } catch (NumberFormatException e) { System.out.println("Enter a valid number."); } }
    private int readPositiveInteger(String prompt) { int value; do { value = readInteger(prompt); if (value <= 0) System.out.println("Enter a positive number."); } while (value <= 0); return value; }
    private String readNonEmptyString(String prompt) { while (true) { System.out.print(prompt); String value = scanner.nextLine().trim(); if (!value.isEmpty()) return value; System.out.println("This field cannot be empty."); } }
    private int readAge() { int age; do { age = readInteger("Age (1-120): "); if (age < 1 || age > 120) System.out.println("Age must be from 1 to 120."); } while (age < 1 || age > 120); return age; }
    private String readGender() { while (true) { String value = readNonEmptyString("Gender (Male/Female/Other): "); if (value.equalsIgnoreCase("male")) return "Male"; if (value.equalsIgnoreCase("female")) return "Female"; if (value.equalsIgnoreCase("other")) return "Other"; System.out.println("Use Male, Female, or Other."); } }
}
