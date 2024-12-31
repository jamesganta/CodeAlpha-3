import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class HotelReservationSystem {

    // Room class
    static class Room {
        String roomNumber;
        String roomType;
        double pricePerNight;
        boolean isAvailable;

        public Room(String roomNumber, String roomType, double pricePerNight) {
            this.roomNumber = roomNumber;
            this.roomType = roomType;
            this.pricePerNight = pricePerNight;
            this.isAvailable = true;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public String getRoomType() {
            return roomType;
        }

        public double getPricePerNight() {
            return pricePerNight;
        }
    }

    // Reservation class
    static class Reservation {
        String guestName;
        String roomNumber;
        Date checkInDate;
        Date checkOutDate;
        double totalPrice;

        public Reservation(String guestName, String roomNumber, Date checkInDate, Date checkOutDate, double totalPrice) {
            this.guestName = guestName;
            this.roomNumber = roomNumber;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.totalPrice = totalPrice;
        }
    }

    // Hotel class
    static class Hotel {
        String hotelName;
        List<Room> rooms;
        List<Reservation> reservations;

        public Hotel(String hotelName) {
            this.hotelName = hotelName;
            this.rooms = new ArrayList<>();
            this.reservations = new ArrayList<>();
        }

        public void addRoom(Room room) {
            rooms.add(room);
        }

        public List<Room> searchAvailableRooms(String roomType, Date checkInDate, Date checkOutDate) {
            List<Room> availableRooms = new ArrayList<>();
            for (Room room : rooms) {
                if (room.isAvailable && room.roomType.equals(roomType)) {
                    boolean isAvailable = true;
                    for (Reservation reservation : reservations) {
                        if ((checkInDate.compareTo(reservation.checkOutDate) >= 0 && checkOutDate.compareTo(reservation.checkInDate) <= 0) ||
                                (checkInDate.compareTo(reservation.checkInDate) < 0 && checkOutDate.compareTo(reservation.checkInDate) > 0) ||
                                (checkInDate.compareTo(reservation.checkInDate) <= 0 && checkOutDate.compareTo(reservation.checkOutDate) >= 0)) {
                            isAvailable = false;
                            break;
                        }
                    }
                    if (isAvailable) {
                        availableRooms.add(room);
                    }
                }
            }
            return availableRooms;
        }

        public Reservation makeReservation(String guestName, String roomNumber, Date checkInDate, Date checkOutDate) {
            Room room = getRoomByNumber(roomNumber);
            if (room == null) {
                return null;
            }
            long diffInMillies = Math.abs(checkOutDate.getTime() - checkInDate.getTime());
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
            double totalPrice = room.pricePerNight * diffInDays;

            Reservation reservation = new Reservation(guestName, roomNumber, checkInDate, checkOutDate, totalPrice);
            reservations.add(reservation);
            room.isAvailable = false;

            System.out.println("Processing payment for " + totalPrice + "...");
            // ... (Implement payment gateway integration here) ...
            System.out.println("Payment successful.");

            return reservation;
        }

        public Room getRoomByNumber(String roomNumber) {
            for (Room room : rooms) {
                if (room.roomNumber.equals(roomNumber)) {
                    return room;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel("Example Hotel");

        // Add rooms to the hotel
        hotel.addRoom(new Room("101", "Single", 50.0));
        hotel.addRoom(new Room("102", "Double", 80.0));
        hotel.addRoom(new Room("103", "Suite", 150.0));

        while (true) {
            System.out.println("\nHotel Reservation System");
            System.out.println("1. Search for Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline after nextInt()

            switch (choice) {
                case 1:
                    System.out.print("Enter room type: ");
                    String roomType = scanner.nextLine();

                    System.out.print("Enter check-in date (YYYY-MM-DD): ");
                    String checkInStr = scanner.nextLine();
                    Date checkInDate = parseDate(checkInStr);

                    System.out.print("Enter check-out date (YYYY-MM-DD): ");
                    String checkOutStr = scanner.nextLine();
                    Date checkOutDate = parseDate(checkOutStr);

                    List<Room> availableRooms = hotel.searchAvailableRooms(roomType, checkInDate, checkOutDate);
                    if (availableRooms.isEmpty()) {
                        System.out.println("No available rooms found.");
                    } else {
                        System.out.println("Available Rooms:");
                        for (Room room : availableRooms) {
                            System.out.println("Room Number: " + room.getRoomNumber() + ", Room Type: " + room.getRoomType() + ", Price: " + room.getPricePerNight());
                        }
                    }
                    break;
                case 2:
                    System.out.print("Enter guest name: ");
                    String guestName = scanner.nextLine();

                    System.out.print("Enter room number: ");
                    String roomNumber = scanner.nextLine();

                    System.out.print("Enter check-in date (YYYY-MM-DD): ");
                    checkInStr = scanner.nextLine();
                    checkInDate = parseDate(checkInStr);

                    System.out.print("Enter check-out date (YYYY-MM-DD): ");
                    checkOutStr = scanner.nextLine();
                    checkOutDate = parseDate(checkOutStr);

                    Reservation reservation = hotel.makeReservation(guestName, roomNumber, checkInDate, checkOutDate);
                    if (reservation != null) {
                        System.out.println("Reservation successful!");
                        // Display reservation details (implement this)
                    } else {
                        System.out.println("Reservation failed. Please try again.");
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Helper method to parse date from String
    private static Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return null;
        }
    }
}