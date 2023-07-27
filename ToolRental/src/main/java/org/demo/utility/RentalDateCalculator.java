package org.demo.utility;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

/**
 * RentalDateCalculator is used to for doing various holiday, weekday and weekend calculations for date ranges
 */
public class RentalDateCalculator {
    /**
     * Calculates the number of weekdays between the startDate and endDate (inclusive)
     */
    public int getWeekDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Adding 1 because we need to include both the start and end date
        long daysBetweenDates = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        DayOfWeek startDay = startDate.getDayOfWeek();
        DayOfWeek returnDay = endDate.getDayOfWeek();

        // No calculation needed if its a full week
        if (daysBetweenDates == 7) return 5;
        if (daysBetweenDates > 7) {
            int partialWeekDays = 0;
            if (isWeekDay(startDay)) {
                // Calculate partial days from startDay week
                // Eg. startDay is tuesday. Friday(5) - Tuesday(2) + 1 = 4 partial weekdays
                partialWeekDays += DayOfWeek.FRIDAY.getValue() - startDay.getValue() + 1;
            }

            // Calculating partial weekdays for checkout day week
            // Since a week won't have more than 5 weekdays, set that as the minimum
            partialWeekDays += Math.min(5, returnDay.getValue());

            // Subtract partial weekdays from all the days and divide it by 7 to get number of full weeks
            long fullWeeks = (daysBetweenDates - partialWeekDays) /7;
            // Calculate weekdays in full weeks
            long weekdaysInFullWeeks = fullWeeks*5;

            // Add partial weekdays and weekdays in the full weeks
            return (int) (weekdaysInFullWeeks + partialWeekDays);
        } else {
            int weekDayCount = 0;
            // Iterate through all the days and count. Worst case will be 6 iterations
            for (int i=0; i<daysBetweenDates; i++) {
                int dayValue =((startDay.ordinal() + i) % 7)+1;
                DayOfWeek day = DayOfWeek.of(dayValue);
                if (isWeekDay(day)) weekDayCount++;
            }
            return weekDayCount;
        }
    }

    /**
     * Calculates the number of weekdays between the startDate and endDate (inclusive)
     * @return Returns number of weekend days
     */
    public int getWeekEndDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Adding 1 because we need to include both the start and end date
        long daysBetweenDates = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        // Use the weekdays method to make the calculation simpler
        return (int) (daysBetweenDates - getWeekDaysBetweenDates(startDate, endDate));
    }

    /**
     * Checks if a day is a weekday or not
     */
    public boolean isWeekDay(DayOfWeek dayOfWeek) {
        return !(DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek));
    }

    /**
     * Gets the list of valid holidays between dates
     * @return Set of holidays
     */
    public Set<LocalDate> getHolidaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> holidays = new HashSet<>();
        for (int year=startDate.getYear(); year <= endDate.getYear(); year++){
           Set<LocalDate> allHolidays = getHolidaysForYear(year);
           for (LocalDate holiday: allHolidays) {
               if (dateIsInRange(startDate, endDate, holiday)) holidays.add(holiday);
           }
        }
        return holidays;
    }

    /**
     * Gets all the holidays for a particular year
     * @return Set of holidays
     */
    public Set<LocalDate> getHolidaysForYear(int year) {
       Set<LocalDate> holidays = new HashSet<>();
       holidays.add(getIndependenceDayForYear(year));
       holidays.add(getLaborDayForYear(year));
       return holidays;
    }

    /**
     * Gets the adjusted independence day for the year
     * @return Returns the adjusted independence day
     */
    public LocalDate getIndependenceDayForYear(int year) {
        LocalDate independenceDay = LocalDate.of(year, Month.JULY, 4);
        // Get the closest weekday based on where independence day falls
        if (DayOfWeek.SATURDAY.equals( independenceDay.getDayOfWeek())) {
             independenceDay = independenceDay.minusDays(1);
        } else if(DayOfWeek.SUNDAY.equals( independenceDay.getDayOfWeek())){
             independenceDay = independenceDay .plusDays(1);
        }
        return  independenceDay;
    }


    /**
     * Gets the adjusted labor day for the year
     * @return Returns Labor Day
     */
    public LocalDate getLaborDayForYear(int year) {
        LocalDate firstDayOfSeptember = LocalDate.of(year, Month.SEPTEMBER,1 );
        DayOfWeek dayOfWeek = firstDayOfSeptember.getDayOfWeek();
        // If monday is sept1st then return this date
        if (DayOfWeek.MONDAY.equals(dayOfWeek)) return firstDayOfSeptember;
        // Calculate how many days until monday
        int daysUntilMonday  = DayOfWeek.SUNDAY.getValue() - dayOfWeek.getValue() + 1;
        // Return the first monday of september
        return firstDayOfSeptember.plusDays(daysUntilMonday);
    }

    /**
     * Checks if a date falls within a date range
     */
    private boolean dateIsInRange(LocalDate startDate, LocalDate endDate, LocalDate dateToCheck) {
        return !(dateToCheck.isBefore(startDate) || dateToCheck.isAfter(endDate));
    }
}