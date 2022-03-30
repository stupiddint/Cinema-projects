package cinema;
import java.util.Arrays;
import java.util.Scanner;

class Seat {
    int row;
    int seat;

    Seat(int row, int seat) {
        this.row = row;
        this.seat = seat;
    }
}
class AlreadyTakenSeatException extends Exception {}

class NonExistingSeatException extends Exception {}

public class CinemaRoomManager {
    private final int rows;
    private final int seats;
    private final String[][] seatArray;
    private int purchased = 0;
    private int currentIncome = 0;

    private CinemaRoomManager(int rows, int seat) {
        this.rows = rows;
        this.seats = seat;
        seatArray = createSeats(rows, seat);
    }
    private static String[][] createSeats(int rows, int seats) {
        String[][] seatArray = new String[rows][];

        for (int i = 0; i < rows; i++) {
            String[] row = new String[seats];
            Arrays.fill(row, "S");
            seatArray[i] = row;
        }

        return seatArray;
    }

    private String getTopRow() {
        String[] topRow = new String[this.seats + 1];

        topRow[0] = " ";
        for (int i = 1; i < topRow.length; i++) {
            topRow[i] = String.valueOf(i);
        }

        return String.join(" ", topRow);
    }

    private boolean isFrontHalf(Seat seat) {
        return seat.row <= rows / 2;
    }

    private int totalSeats() {
        return rows * seats;
    }

    private int getPrice(Seat seat) {
        int price;

        if (totalSeats() <= 60 || isFrontHalf(seat)) {
            price = 10;
        } else {
            price = 8;
        }

        return price;
    }

    private void printPrice(int price) {
        System.out.printf("Ticket price: $%s\n", price);
        System.out.println();
    }

    private void takeSeat(Seat seat) throws AlreadyTakenSeatException {
        if (seatArray[seat.row - 1][seat.seat - 1].equals("B")) {
            throw new AlreadyTakenSeatException();
        } else {
            seatArray[seat.row - 1][seat.seat - 1] = "B";
            System.out.println("Thank you!\nyou purchased it.");
        }
    }

    private Seat selectSeat() throws NonExistingSeatException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter a row number:");
        int row = sc.nextInt();

        System.out.println("Enter a seat number in that row:");
        int seat = sc.nextInt();

        System.out.println();

        if (row < 1 || row > rows || seats< 1 || seat > seats) {
            throw new NonExistingSeatException();
        }

        return new Seat(row, seat);
    }

    private void buySeat() {
        Scanner in = new Scanner(System.in);
        try {
            Seat seat = selectSeat();
            int price = getPrice(seat);
            printPrice(price);

            System.out.println("Do you want to book this ticket?");
            System.out.println("1. yes \n0. no");
            System.out.print("Choose Option:");
            int d = in.nextInt();
            if(d==1) {
                takeSeat(seat);
                currentIncome += price;
                purchased++;
            }

        } catch (AlreadyTakenSeatException e) {
            System.out.println("That ticket has already been purchased!");
            System.out.println();
            buySeat();
        } catch (NonExistingSeatException e) {
            System.out.println("Wrong input! ");
            System.out.println();
            buySeat();
        }
    }

    private void printSeats() {
        System.out.println("Cinema:");

        String topRow = getTopRow();
        System.out.println(topRow);

        for (int i = 1; i <= seatArray.length; i++) {
            System.out.printf("%d ", i);
            String row = String.join(" ", seatArray[i - 1]);
            System.out.println(row);
        }
        System.out.println();
    }

    private int getTotalIncome() {
        if (totalSeats() <= 60) {
            return rows * seats * 10;
        } else {
            return (rows / 2) * seats * 10 + (rows - rows / 2) * seats * 8;
        }
    }

    private String getPercentage() {
        double percentage = (double) purchased * 100 / totalSeats();
        return String.format("%.2f", percentage);
    }

    private void printStatistics() {
        System.out.printf("Number of purchased tickets: %d\n", purchased);
        System.out.printf("Percentage: %s%%\n", getPercentage());
        System.out.printf("Current income: $%d\n", currentIncome);
        System.out.printf("Total income: $%d\n", getTotalIncome());
        System.out.println();
    }

    private void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            System.out.print("Choose options: ");
            int input = scanner.nextInt();
            System.out.println();

            switch (input) {
                case 0:
                    return;
                case 1:
                    printSeats();
                    break;
                case 2:
                    buySeat();
                    break;
                case 3:
                    printStatistics();
                    break;
                default:
                    System.out.println(" invalid choice\n");
                    showMenu();

            }
        }
    }

    private static CinemaRoomManager readCinema() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();

        System.out.println("Enter the number of seats in each row:");
        int seats = scanner.nextInt();

        System.out.println();

        return new CinemaRoomManager(rows, seats);
    }

    public static void main(String[] args) {
        CinemaRoomManager cinema = readCinema();
        cinema.showMenu();

    }
}
