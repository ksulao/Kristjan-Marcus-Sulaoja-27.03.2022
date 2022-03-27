package tablecreator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TableCreator {

    private final Scanner scanner = new Scanner(System.in);
    private Integer year = null;
    private List<LocalDate> holidays = new ArrayList<>();
    private final Integer monthsInYear = 12;
    /**
     * Input reader.
     * Check if input valid.
     */
    public void inputReader() {
        System.out.println("Create payment and accountant remainder dates table of what year? ");
        while (year == null) {
            String input = scanner.next();
            if (input.matches("[0-9]+")) {
                year = Integer.parseInt(input);
            } else {
                System.out.println("Wrong input");
            }
        }
    }

    /**
     * Creates table according to dates.
     * Return statement is needed for testing.
     * @return List of table data strings
     */
    public List<String> createTable() {
        createHolidays();
        List<String> tableData = new ArrayList<>();
        tableData.add("PAYMENT DATE,REMINDER DATE");
        for (int n = 1; n <= monthsInYear; n++) {
            LocalDate date = LocalDate.of(year, n, 10);
            LocalDate paymentDate = getSalaryPaymentDay(date);
            LocalDate reminderDate = getAccountantReminderDay(date);
            tableData.add("%02d.%s,%02d.%s".formatted(paymentDate.getDayOfMonth(), paymentDate.getMonth().toString(),
                    reminderDate.getDayOfMonth(), reminderDate.getMonth().toString()));
        }
        csvWritter(tableData, year.toString());
        System.out.println(String.join("\n", tableData));
        return tableData;
    }

    /**
     * Change original date if it is on holiday or weekend.
     * @param date original date
     * @return correct payment date
     */
    public LocalDate getSalaryPaymentDay(LocalDate date) {
        while (checkIfHoliday(date) || checkIfWeekend(date)) {
            date = changeIfWeekend(date);
            date = changeIfHoliday(date);
        }
        return date;
    }

    /**
     * Find a date that is 3 business days before the original date.
     * @param date original date
     * @return correct reminder date
     */
    public LocalDate getAccountantReminderDay(LocalDate date) {
        while (checkIfHoliday(date) || checkIfWeekend(date)) {
            date = changeIfWeekend(date);
            date = changeIfHoliday(date);
        }
        String dayOfWeek = date.getDayOfWeek().toString();
        if ("MONDAY".equals(dayOfWeek) || "TUESDAY".equals(dayOfWeek)
                || "WEDNESDAY".equals(dayOfWeek)) {
            date = date.minusDays(5);
        } else {
            date = date.minusDays(3);
        }
        return date;
    }

    /**
     * Check whether date is on the weekend.
     * @param date original date
     * @return true if date is on the weekend
     */
    private boolean checkIfWeekend(LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().toString();
        return "SUNDAY".equals(dayOfWeek) || "SATURDAY".equals(dayOfWeek);
    }

    /**
     * Check whether date is on the holiday.
     * @param date original date
     * @return true if date is on the holiday
     */
    private boolean checkIfHoliday(LocalDate date) {
        return holidays.contains(date);
    }

    /**
     * Change original date if it is on weekend.
     * @param date original date
     * @return correct payment date
     */
    private LocalDate changeIfWeekend(LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().toString();
        if ("SUNDAY".equals(dayOfWeek)) {
            date = date.minusDays(2);
        } else if ("SATURDAY".equals(dayOfWeek)) {
            date = date.minusDays(1);
        }
        return date;
    }

    /**
     * Change original date if it is on holiday.
     * @param date original date
     * @return correct payment date
     */
    private LocalDate changeIfHoliday(LocalDate date) {
        if (checkIfHoliday(date)) {
            date = date.minusDays(1);
        }
        return date;
    }

    /**
     * Create all holiday LocalDates.
     * Easter, Whit Sunday and Good Friday are the only movable dates.
     * Easter is calculated using method getEasterDate()
     * Whit Sunday is seven weeks after Easter
     * Good Friday is 2 days before Easter
     */
    private void createHolidays() {
        holidays.add(LocalDate.of(year, 1, 1)); // New year
        holidays.add(LocalDate.of(year, 2, 24)); // Independence Day
        holidays.add(LocalDate.of(year, 5, 1)); // Spring Day
        holidays.add(LocalDate.of(year, 6, 23)); // Victory Day
        holidays.add(LocalDate.of(year, 6, 24)); // Midsummer day
        holidays.add(LocalDate.of(year, 8, 20)); // Independence Restoration Day
        holidays.add(LocalDate.of(year, 12, 24)); // Christmas Eve
        holidays.add(LocalDate.of(year, 12, 25)); // Christmas Day
        holidays.add(LocalDate.of(year, 12, 26)); // Boxing day
        // Movable holidays
        LocalDate easterDate = getEasterDate();
        holidays.add(easterDate); // Easter
        holidays.add(easterDate.plusWeeks(7)); // Whit Sunday
        holidays.add(easterDate.minusDays(2)); // Good Friday
    }

    /**
     * Formula for calculating Easter date.
     * @return LocalDate of Easter
     */
    private LocalDate getEasterDate() {
        int remainderA = year % 19;
        int quotientB = year / 100;
        int remainderC = year % 100;
        int quotientD = quotientB / 4;
        int remainderE = quotientB % 4;
        int quotientG = (8 * quotientB + 13) / 25;
        int remainderH = (19 * remainderA + quotientB - quotientD - quotientG + 15) % 30;
        int quotientJ = remainderC / 4;
        int remainderK = remainderC % 4;
        int quotientM = (remainderA + 11 * remainderH) / 319;
        int remainderR = (2 * remainderE + 2 * quotientJ - remainderK - remainderH + quotientM + 32) % 7;
        int month = (remainderH - quotientM + remainderR + 90) / 25;
        int day = (remainderH - quotientM + remainderR + month + 19) % 32;
        return LocalDate.of(year, month, day);
    }

    /**
     * Csv writer for writing csv files
     * @param tableData List of strings
     * @param filename Year of the created table
     */
    private void csvWritter(List<String> tableData, String filename) {
        if (filename != null) {
            String homeDir = System.getProperty("user.home");
            Path path = Paths.get(homeDir
                    + "\\Kristjan-Marcus-Sulaoja-27.03.2022\\src\\csvfiles\\" + filename);
            try (PrintWriter printWriter = new PrintWriter(path.toString())) {
                for (String line : tableData) {
                    printWriter.println(line);
                }
            } catch (IOException ex) {
                System.out.println("Error creating csv file");
            }
        }
    }

    /**
     * Set year, used for testing.
     * @param year Integer
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * Main method.
     * Activate the program.
     * @param args args
     */
    public static void main(String[] args) throws IOException {
        TableCreator tableCreator = new TableCreator();
        tableCreator.inputReader();
        tableCreator.createTable();
    }
}
