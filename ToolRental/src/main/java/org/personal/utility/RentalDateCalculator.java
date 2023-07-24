package org.personal.utility;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

// TODO Might want to make this static
public class RentalDateCalculator {
    public int getWeekDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Adding 1 because we need to include both the start and end date
        long daysBetweenDates = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        DayOfWeek startDay = startDate.getDayOfWeek();
        DayOfWeek returnDay = endDate.getDayOfWeek();

        if (daysBetweenDates == 7) return 5;
        if (daysBetweenDates > 7) {
            int partialWeekDays = 0;
            // Calculate partial days from startDay week
            if (isWeekDay(startDay)) {
                partialWeekDays += DayOfWeek.FRIDAY.getValue() - startDay.getValue() + 1;
            }

            // Calculating partial weekdays for checkout day week
            // If it's a weekend we just want to take 5 as a value since that's how many weekdays we'll have that week
            partialWeekDays += Math.min(5, returnDay.getValue());

            // Subtract partial weekdays from all the days and divide it by 7 to get number of full weeks
            long fullWeeks = (daysBetweenDates - partialWeekDays) /7;
            long weekdaysInFullWeeks = fullWeeks*5;

            return (int) (weekdaysInFullWeeks + partialWeekDays);
        } else {
            int weekDayCount = 0;
            // Iterate through all the days and count the days
            // This should be constant time, decided to do this be
            for (int i=0; i<daysBetweenDates; i++) {
                int dayValue =((startDay.ordinal() + i) % 7)+1;
                DayOfWeek day = DayOfWeek.of(dayValue);
                if (isWeekDay(day)) weekDayCount++;
            }
            return weekDayCount;
        }
    }

    public int getWeekEndDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Adding 1 because we need to include both the start and end date
        long daysBetweenDates = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return (int) (daysBetweenDates - getWeekDaysBetweenDates(startDate, endDate));
    }

    public boolean isWeekDay(DayOfWeek dayOfWeek) {
        return !(DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek));
    }

    public boolean isWeekDay(LocalDate date) {
        return isWeekDay(date.getDayOfWeek());
    }

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

    public Set<LocalDate> getHolidaysForYear(int year) {
       Set<LocalDate> holidays = new HashSet<>();
       holidays.add(getIndependenceDayForYear(year));
       holidays.add(getLaborDayForYear(year));
       return holidays;
    }
    public LocalDate getIndependenceDayForYear(int year) {
        return LocalDate.of(year, Month.JULY, 4);
    }

    public LocalDate getLaborDayForYear(int year) {
        LocalDate firstDayOfSeptember = LocalDate.of(year, Month.SEPTEMBER,1 );
        DayOfWeek dayOfWeek = firstDayOfSeptember.getDayOfWeek();

        if (DayOfWeek.MONDAY.equals(dayOfWeek)) return firstDayOfSeptember;
        int daysUntilMonday  = DayOfWeek.SUNDAY.getValue() - dayOfWeek.getValue() + 1;
        return firstDayOfSeptember.plusDays(daysUntilMonday);
    }

    private boolean dateIsInRange(LocalDate startDate, LocalDate endDate, LocalDate dateToCheck) {
        return !(dateToCheck.isBefore(startDate) || dateToCheck.isAfter(endDate));
    }
}