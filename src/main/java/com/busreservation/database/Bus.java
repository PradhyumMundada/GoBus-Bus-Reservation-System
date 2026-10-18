package com.busreservation.database;

/** Simple model class representing a row in the buses table. */
public class Bus {
    private final int busId;
    private final String busNumber;
    private final String busName;
    private final String source;
    private final String destination;
    private final String departureTime;
    private final String arrivalTime;
    private final int totalSeats;
    private final int availableSeats;
    private final double fare;

    public Bus(int busId, String busNumber, String busName, String source, String destination,
               String departureTime, String arrivalTime, int totalSeats, int availableSeats, double fare) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.busName = busName;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.fare = fare;
    }

    public int getBusId() { return busId; }
    public String getBusNumber() { return busNumber; }
    public String getBusName() { return busName; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public double getFare() { return fare; }

    public String display() {
        return String.format("%d | %s | %s | %s -> %s | %s - %s | Available: %d | Fare: Rs. %.2f",
                busId, busNumber, busName, source, destination, departureTime, arrivalTime, availableSeats, fare);
    }
}
