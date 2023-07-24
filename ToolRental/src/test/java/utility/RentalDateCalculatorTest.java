package utility;

import org.junit.jupiter.api.Test;
import org.personal.utility.RentalDateCalculator;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RentalDateCalculatorTest {
    RentalDateCalculator rentalDateCalculator = new RentalDateCalculator();

    @Test
    public void calculateWeekDaysForFullWeek(){
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);
        long daysOfWeek = rentalDateCalculator.getWeekDaysBetweenDates(startDate, endDate);
        assertEquals(5, daysOfWeek);
    }

    @Test
    public void calculateWeekDaysForPartialWeeks(){
        LocalDate startDate = LocalDate.parse("2023-07-20");
        LocalDate endDate = LocalDate.parse("2023-07-25");
        long daysOfWeek = rentalDateCalculator.getWeekDaysBetweenDates(startDate, endDate);
        assertEquals(4, daysOfWeek);
    }

    @Test
    public void calculateWeekDaysForMultipleWeeks(){
        LocalDate startDate = LocalDate.parse("2023-07-06");
        LocalDate endDate = LocalDate.parse("2023-07-25");
        long daysOfWeek = rentalDateCalculator.getWeekDaysBetweenDates(startDate, endDate);
        assertEquals(14, daysOfWeek);
    }


    @Test
    public void calculateWeekDaysStartsOnWeekend(){
        LocalDate startDate = LocalDate.parse("2023-07-08");
        LocalDate endDate = LocalDate.parse("2023-07-25");
        long daysOfWeek = rentalDateCalculator.getWeekDaysBetweenDates(startDate, endDate);
        assertEquals(12, daysOfWeek);
    }
    @Test
    public void calculateWeekDaysEndsOnWeekend(){
        LocalDate startDate = LocalDate.parse("2023-07-07");
        LocalDate endDate = LocalDate.parse("2023-07-23");
        long daysOfWeek = rentalDateCalculator.getWeekDaysBetweenDates(startDate, endDate);
        assertEquals(11, daysOfWeek);
    }


    @Test
    public void calculateWeekDaysBothWeekend(){
        LocalDate startDate = LocalDate.parse("2023-07-08");
        LocalDate endDate = LocalDate.parse("2023-07-23");
        long daysOfWeek = rentalDateCalculator.getWeekDaysBetweenDates(startDate, endDate);
        assertEquals(10, daysOfWeek);
    }


    @Test
    public void calculateWeekendDays(){
        LocalDate startDate = LocalDate.parse("2023-07-06");
        LocalDate endDate = LocalDate.parse("2023-07-23");
        long daysOfWeek = rentalDateCalculator.getWeekEndDaysBetweenDates(startDate, endDate);
        assertEquals(6, daysOfWeek);
    }


    @Test
    public void calculateWeekendDaysNoWeekends(){
        LocalDate startDate = LocalDate.parse("2023-07-03");
        LocalDate endDate = LocalDate.parse("2023-07-07");
        long daysOfWeek = rentalDateCalculator.getWeekEndDaysBetweenDates(startDate, endDate);
        assertEquals(0, daysOfWeek);
    }

    @Test
    public void calculateWeekendDaysPartialWeek(){
        LocalDate startDate = LocalDate.parse("2023-07-07");
        LocalDate endDate = LocalDate.parse("2023-07-11");
        long daysOfWeek = rentalDateCalculator.getWeekEndDaysBetweenDates(startDate, endDate);
        assertEquals(2, daysOfWeek);
    }

    @Test
    public void calculateWeekendDaysFullWeeks(){
        LocalDate startDate = LocalDate.parse("2023-07-06");
        LocalDate endDate = LocalDate.parse("2023-07-25");
        long daysOfWeek = rentalDateCalculator.getWeekEndDaysBetweenDates(startDate, endDate);
        assertEquals(6, daysOfWeek);
    }
}
