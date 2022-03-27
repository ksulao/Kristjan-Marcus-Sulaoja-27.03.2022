package testTableCreator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tablecreator.TableCreator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tester uses also other dates than the 10th day of each month.
 * It is better that way to show code reliability with special cases.
 */
public class TestTableCreator {
    private TableCreator tableCreator;

    @BeforeEach
    void doBeforeEachTest() {
        tableCreator = new TableCreator();
    }

    @Test
    void testPaymentWeekdayNoHolidays() {
        LocalDate date1 = LocalDate.of(2022, 5, 10);
        LocalDate date2 = LocalDate.of(2025, 10, 10);
        LocalDate expectedDate1Payment = LocalDate.of(2022, 5, 10);
        LocalDate expectedDate2Payment = LocalDate.of(2025, 10, 10);
        assertEquals(expectedDate1Payment, tableCreator.getSalaryPaymentDay(date1));
        assertEquals(expectedDate2Payment, tableCreator.getSalaryPaymentDay(date2));
    }

    @Test
    void testRemainderWeekdayNoHolidays() {
        LocalDate date1 = LocalDate.of(2022, 5, 10);
        LocalDate date2 = LocalDate.of(2025, 10, 10);
        LocalDate expectedDate1Reminder = LocalDate.of(2022, 5, 5);
        LocalDate expectedDate2Reminder = LocalDate.of(2025, 10, 7);
        assertEquals(expectedDate1Reminder, tableCreator.getAccountantReminderDay(date1));
        assertEquals(expectedDate2Reminder, tableCreator.getAccountantReminderDay(date2));
    }

    @Test
    void testPaymentSundaysNoHolidays() {
        LocalDate date1 = LocalDate.of(2022, 5, 8);
        LocalDate date2 = LocalDate.of(2025, 10, 26);
        LocalDate expectedDate1Payment = LocalDate.of(2022, 5, 6);
        LocalDate expectedDate2Payment = LocalDate.of(2025, 10, 24);
        assertEquals(expectedDate1Payment, tableCreator.getSalaryPaymentDay(date1));
        assertEquals(expectedDate2Payment, tableCreator.getSalaryPaymentDay(date2));
    }

    @Test
    void testRemainderSundaysNoHolidays() {
        LocalDate date1 = LocalDate.of(2022, 5, 8);
        LocalDate date2 = LocalDate.of(2025, 10, 26);
        LocalDate expectedDate1Reminder = LocalDate.of(2022, 5, 3);
        LocalDate expectedDate2Reminder = LocalDate.of(2025, 10, 21);
        assertEquals(expectedDate1Reminder, tableCreator.getAccountantReminderDay(date1));
        assertEquals(expectedDate2Reminder, tableCreator.getAccountantReminderDay(date2));
    }

    @Test
    void testPaymentSaturdayNoHolidays() {
        LocalDate date1 = LocalDate.of(2022, 5, 7);
        LocalDate date2 = LocalDate.of(2025, 10, 25);
        LocalDate expectedDate1Payment = LocalDate.of(2022, 5, 6);
        LocalDate expectedDate2Payment = LocalDate.of(2025, 10, 24);
        assertEquals(expectedDate1Payment, tableCreator.getSalaryPaymentDay(date1));
        assertEquals(expectedDate2Payment, tableCreator.getSalaryPaymentDay(date2));
    }

    @Test
    void testRemainderSaturdayNoHolidays() {
        LocalDate date1 = LocalDate.of(2022, 5, 7);
        LocalDate date2 = LocalDate.of(2025, 10, 25);
        LocalDate expectedDate1Reminder = LocalDate.of(2022, 5, 3);
        LocalDate expectedDate2Reminder = LocalDate.of(2025, 10, 21);
        assertEquals(expectedDate1Reminder, tableCreator.getAccountantReminderDay(date1));
        assertEquals(expectedDate2Reminder, tableCreator.getAccountantReminderDay(date2));
    }

    @Test
    void testPaymentAndReminderOnDifferentMonths() {
        LocalDate date = LocalDate.of(2019, 1, 3); // Thursday
        LocalDate expectedDatePayment = LocalDate.of(2019, 1, 3); // Thursday
        LocalDate expectedDateReminder = LocalDate.of(2018, 12, 31); // Monday
        assertEquals(expectedDatePayment, tableCreator.getSalaryPaymentDay(date));
        assertEquals(expectedDateReminder, tableCreator.getAccountantReminderDay(date));
    }

    @Test
    void testPaymentOnHoliday() {
        LocalDate date = LocalDate.of(2022, 4, 15); // Good Friday, Friday
        LocalDate expected = LocalDate.of(2022, 4, 14); // Thursday
        tableCreator.setYear(2022);
        tableCreator.createTable();
        assertEquals(expected, tableCreator.getSalaryPaymentDay(date));
    }

    @Test
    void testRemainderOnHoliday() {
        LocalDate date = LocalDate.of(2022, 4, 15);  // Good Friday, Friday
        LocalDate expected = LocalDate.of(2022, 4, 11); // Monday
        tableCreator.setYear(2022);
        tableCreator.createTable();
        assertEquals(expected, tableCreator.getAccountantReminderDay(date));
    }

    @Test
    void testPaymentOnHolidayAfterWeekend() {
        LocalDate date = LocalDate.of(2018, 8, 20); // Independence Restoration Day, Monday
        LocalDate expected = LocalDate.of(2018, 8, 17); // Friday
        tableCreator.setYear(2018);
        tableCreator.createTable();
        assertEquals(expected, tableCreator.getSalaryPaymentDay(date));
    }

    @Test
    void testRemainderOnHolidayAfterWeekend() {
        LocalDate date = LocalDate.of(2018, 8, 20);  // Independence Restoration Day, Monday
        LocalDate expected = LocalDate.of(2018, 8, 14); // Tuesday
        tableCreator.setYear(2018);
        tableCreator.createTable();
        assertEquals(expected, tableCreator.getAccountantReminderDay(date));
    }

    @Test
    void testPaymentTwoHolidaysInSuccession() {
        LocalDate date = LocalDate.of(2020, 12, 25); // Christmas Day, Friday
        LocalDate expected = LocalDate.of(2020, 12, 23); // Wednesday
        tableCreator.setYear(2020);
        tableCreator.createTable();
        assertEquals(expected, tableCreator.getSalaryPaymentDay(date));
    }

    @Test
    void testRemainderTwoHolidaysInSuccession() {
        LocalDate date = LocalDate.of(2020, 12, 25);  // Christmas Day, Friday
        LocalDate expected = LocalDate.of(2020, 12, 18); // Friday
        tableCreator.setYear(2020);
        tableCreator.createTable();
        assertEquals(expected, tableCreator.getAccountantReminderDay(date));
    }

    @Test
    void testCreateTable() {
        tableCreator.setYear(2020);
        List<String> expected = new ArrayList<>((List.of("PAYMENT DATE,REMINDER DATE", "10.JANUARY,07.JANUARY",
                "10.FEBRUARY,05.FEBRUARY", "10.MARCH,05.MARCH",
                "09.APRIL,06.APRIL", "08.MAY,05.MAY", "10.JUNE,05.JUNE",
                "10.JULY,07.JULY", "10.AUGUST,05.AUGUST", "10.SEPTEMBER,07.SEPTEMBER",
                "09.OCTOBER,06.OCTOBER", "10.NOVEMBER,05.NOVEMBER", "10.DECEMBER,07.DECEMBER")));
        List<String> tableData = tableCreator.createTable();
        assertEquals(expected, tableData);
    }
}
