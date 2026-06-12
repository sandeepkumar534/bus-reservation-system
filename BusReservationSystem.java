
import java.util.ArrayList;
import java.util.Scanner;

class SeatAlreadyBookedException extends Exception {
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}

class Bus {
    private int busId;
    private String busName;
    private String source;
    private String destination;
    private double acFare;
    private double nonAcFare;

    public Bus(int busId, String busName, String source,
               String destination, double acFare, double nonAcFare) {
        this.busId = busId;
        this.busName = busName;
        this.source = source;
        this.destination = destination;
        this.acFare = acFare;
        this.nonAcFare = nonAcFare;
    }

    public int getBusId() { return busId; }
    public String getBusName() { return busName; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public double getAcFare() { return acFare; }
    public double getNonAcFare() { return nonAcFare; }

    public void displayBus() {
        System.out.println("--------------------------------------");
        System.out.println("Bus ID      : " + busId);
        System.out.println("Bus Name    : " + busName);
        System.out.println("Source      : " + source);
        System.out.println("Destination : " + destination);
        System.out.println("AC Fare     : Rs." + acFare);
        System.out.println("Non-AC Fare : Rs." + nonAcFare);
        System.out.println("--------------------------------------");
    }
}

class Seat {
    private int seatNumber;
    private boolean booked;

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.booked = false;
    }

    public int getSeatNumber() { return seatNumber; }
    public boolean isBooked() { return booked; }
    public void bookSeat() { booked = true; }
}

class Passenger {
    private String name;
    private int age;
    private String gender;

    public Passenger(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() { return name; }
}

class Booking {
    private String bookingId;
    private Passenger passenger;
    private Bus bus;
    private Seat seat;
    private String ticketType;
    private double fare;

    public Booking(String bookingId, Passenger passenger, Bus bus,
                   Seat seat, String ticketType, double fare) {
        this.bookingId = bookingId;
        this.passenger = passenger;
        this.bus = bus;
        this.seat = seat;
        this.ticketType = ticketType;
        this.fare = fare;
    }

    public void printTicket() {
        System.out.println("\n=================================");
        System.out.println("         BUS TICKET");
        System.out.println("=================================");
        System.out.println("Booking ID : " + bookingId);
        System.out.println("Passenger  : " + passenger.getName());
        System.out.println("Bus        : " + bus.getBusName());
        System.out.println("Ticket Type: " + ticketType);
        System.out.println("Seat No    : " + seat.getSeatNumber());
        System.out.println("Source     : " + bus.getSource());
        System.out.println("Destination: " + bus.getDestination());
        System.out.println("Fare       : Rs." + fare);
        System.out.println("Status     : Confirmed");
        System.out.println("=================================");
    }
}

class BookingTimeoutThread extends Thread {
    public void run() {
        try {
            Thread.sleep(10000);
            System.out.println("\n[INFO] Booking session timeout after 10 seconds.");
        } catch (InterruptedException e) {
        }
    }
}

public class BusReservationSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Booking> bookings = new ArrayList<>();
        ArrayList<Bus> buses = new ArrayList<>();

        buses.add(new Bus(101, "VRL Travels", "Bangalore", "Pune", 1200, 900));
        buses.add(new Bus(102, "SRS Travels", "Bangalore", "Mumbai", 1300, 1000));
        buses.add(new Bus(103, "Orange Travels", "Bangalore", "Kochi", 1400, 1100));
        buses.add(new Bus(104, "KPN Travels", "Bangalore", "Bhubaneswar", 1700, 1400));

        Seat[] seats = new Seat[20];
        for (int i = 0; i < 20; i++) {
            seats[i] = new Seat(i + 1);
        }

        while (true) {
            System.out.println("\n=================================");
            System.out.println(" BUS TICKET RESERVATION SYSTEM");
            System.out.println("=================================");
            System.out.println("1. View Buses");
            System.out.println("2. Book Ticket");
            System.out.println("3. View Total Bookings");
            System.out.println("4. Exit");

            System.out.print("\nEnter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    for (Bus bus : buses) bus.displayBus();
                    break;

                case 2:
                    for (Bus bus : buses) bus.displayBus();

                    System.out.print("\nEnter Bus ID: ");
                    int busId = sc.nextInt();

                    Bus selectedBus = null;
                    for (Bus bus : buses) {
                        if (bus.getBusId() == busId) {
                            selectedBus = bus;
                            break;
                        }
                    }

                    if (selectedBus == null) {
                        System.out.println("Invalid Bus ID!");
                        break;
                    }

                    System.out.println("\nSelect Ticket Type");
                    System.out.println("1. AC");
                    System.out.println("2. Non-AC");
                    System.out.print("Enter Choice: ");
                    int typeChoice = sc.nextInt();

                    String ticketType;
                    double fare;

                    if (typeChoice == 1) {
                        ticketType = "AC";
                        fare = selectedBus.getAcFare();
                    } else {
                        ticketType = "Non-AC";
                        fare = selectedBus.getNonAcFare();
                    }

                    System.out.println("\nAvailable Seats:");
                    for (Seat seat : seats) {
                        if (!seat.isBooked()) {
                            System.out.print(seat.getSeatNumber() + " ");
                        }
                    }

                    System.out.print("\n\nEnter Seat Number: ");
                    int seatNo = sc.nextInt();

                    try {
                        if (seats[seatNo - 1].isBooked()) {
                            throw new SeatAlreadyBookedException("Seat Already Booked!");
                        }

                        BookingTimeoutThread timeout = new BookingTimeoutThread();
                        timeout.start();

                        sc.nextLine();

                        System.out.print("Passenger Name: ");
                        String name = sc.nextLine();

                        System.out.print("Age: ");
                        int age = sc.nextInt();

                        sc.nextLine();

                        System.out.print("Gender: ");
                        String gender = sc.nextLine();

                        Passenger passenger = new Passenger(name, age, gender);

                        seats[seatNo - 1].bookSeat();
                        timeout.interrupt();

                        String bookingId = "BK" + (1000 + bookings.size() + 1);

                        Booking booking = new Booking(
                                bookingId,
                                passenger,
                                selectedBus,
                                seats[seatNo - 1],
                                ticketType,
                                fare);

                        bookings.add(booking);
                        booking.printTicket();

                    } catch (SeatAlreadyBookedException e) {
                        System.out.println(e.getMessage());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid Seat Number!");
                    }
                    break;

                case 3:
                    System.out.println("\nTotal Bookings : " + bookings.size());
                    break;

                case 4:
                    System.out.println("\nThank You For Using The System!");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
