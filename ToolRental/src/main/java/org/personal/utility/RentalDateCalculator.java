package org.personal.utility;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalDateCalculator {
    public long getWeekDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Adding 1 because we need to include both the start and end date
        long daysBetweenDates = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        DayOfWeek checkOutDay = startDate.getDayOfWeek();
        DayOfWeek returnDay = endDate.getDayOfWeek();

        if (daysBetweenDates == 7) return 5;
        if (daysBetweenDates > 7) {
            int partialWeekDays = 0;
            // Calculate partial days from checkOutDay week
            if (!isWeekend(checkOutDay)) {
                partialWeekDays += DayOfWeek.FRIDAY.getValue() - checkOutDay.getValue() + 1;
            }

            // Calculating partial weekdays for checkout day week
            // If it's a weekend we just want to take 5 as a value since that's how many weekdays we'll have that week
            partialWeekDays += Math.min(5, returnDay.getValue());

            // Subtract partial weekdays from all the days and divide it by 7 to get number of full weeks
            long fullWeeks = (daysBetweenDates - partialWeekDays) /7;
            long weekdaysInFullWeeks = fullWeeks*5;

            return weekdaysInFullWeeks + partialWeekDays;
        } else {
            int weekDayCount = 0;
            // Iterate through all the days and count the days
            // This should be constant time, decided to do this be
            for (int i=0; i<daysBetweenDates; i++) {
                int dayValue =((checkOutDay.ordinal() + i) % 7)+1;
                DayOfWeek day = DayOfWeek.of(dayValue);
                if (!isWeekend(day)) weekDayCount++;
            }
            return weekDayCount;
        }
    }

    public long getWeekEndDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Adding 1 because we need to include both the start and end date
        long daysBetweenDates = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return daysBetweenDates - getWeekDaysBetweenDates(startDate, endDate);
    }

    private boolean isWeekend(DayOfWeek dayOfWeek) {
        return DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek);
    }
}