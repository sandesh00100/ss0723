package org.personal;

import lombok.Value;
import org.apache.commons.cli.*;
import org.personal.enums.Tool;
import org.personal.enums.ToolType;
import org.personal.exceptions.CheckoutValidationException;
import org.personal.exceptions.InvalidCommandLineArgumentException;
import org.personal.models.CheckOut;
import org.personal.models.RentalAgreement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.Date;

@Value
public class ToolRentalUtility {
    private static Logger logger = LoggerFactory.getLogger(ToolRentalUtility.class);
    public static void main(String ...args) {
        logger = LoggerFactory.getLogger(ToolRentalUtility.class);
        logger.debug("Arguments entered " + Arrays.toString(args));

        Options options = new Options()
                .addOption(Option.builder().option("t").longOpt("toolCode").desc("Unique identifier for a tool (eg. 'CHNS' or 'LADW')").required().build())
                .addOption(Option.builder().option("r").longOpt("rentalDayCount").desc("Number of days for rental. Any number from 1-100").required().build())
                .addOption(Option.builder().option("d").longOpt("discountPercentage").desc("Percentage discounted. Any number from 0-100 (eg. 20 = 20% discount)").required().build())
                .addOption(Option.builder().option("c").longOpt("checkoutDate").desc("Date in the MM/DD/YY format. (eg. 07/20/23)").required().build());

        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            ToolRentalUtility toolRentalUtility = new ToolRentalUtility();
            RentalAgreement rentalAgreement = toolRentalUtility.calculateRentalAgreement(line);
        }
        catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            System.exit(-1);
        } catch (InvalidCommandLineArgumentException e) {
            // TODO: Think about just using the exception message
            System.out.println("Argument: " + e.getArgumentName() + " could not be parsed");
            System.exit(-1);
        } catch (CheckoutValidationException e) {
            System.out.println("Checkout field: " + e.getCheckoutField() + " is not valid because " + e.getValidationFailure());
            System.exit(-1);
        }

    }

    public RentalAgreement calculateRentalAgreement(CommandLine line) throws InvalidCommandLineArgumentException, CheckoutValidationException {
        Tool tool = Tool.getToolByCode(line.getOptionValue('t'));
        if (tool == null) throw new InvalidCommandLineArgumentException("toolCode");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDate checkoutDate;
        int rentalDayCount;
        int discountPercentage;

        try {
            rentalDayCount = Integer.parseInt(line.getOptionValue('r'));
        } catch (Exception e) {
            throw new InvalidCommandLineArgumentException("checkOutDate", e);
        }
        try {
            discountPercentage = Integer.parseInt(line.getOptionValue('d'));
        } catch (Exception e) {
            throw new InvalidCommandLineArgumentException("checkOutDate", e);
        }
        try {
            checkoutDate = LocalDate.parse(line.getOptionValue('c'), formatter);
        } catch (Exception e) {
           throw new InvalidCommandLineArgumentException("checkOutDate", e);
        }

        CheckOut checkOut = CheckOut.builder()
                .tool(tool)
                .rentalDayCount(rentalDayCount)
                .discountPercentage(discountPercentage)
                .checkOutDate(checkoutDate)
                .build();

        return calculateRentalAgreement(checkOut);
    }

    public RentalAgreement calculateRentalAgreement(CheckOut checkOut) throws CheckoutValidationException {
        validateCheckout(checkOut);

        return RentalAgreement.builder().build();
    }

    private int getChargableDays(ToolType toolType, LocalDate checkOutDate, LocalDate returnDate) {
        // TODO Finish this
        return -1;
    }

    private long getWeekEndDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Adding 1 because we need to include both the start and end date
        long daysBetweenDates = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return daysBetweenDates - getWeekDaysBetweenDates(startDate, endDate);
    }

    // TODO revisit this
    private long getWeekDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Adding 1 because we need to include both the start and end date
        long daysBetweenDates = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        DayOfWeek checkOutDay = startDate.getDayOfWeek();
        DayOfWeek returnDay = endDate.getDayOfWeek();

        long weekDayCount = 0;
        if (daysBetweenDates == 7) return 5;
        if (daysBetweenDates > 7) {
            if (!isWeekend(checkOutDay)) {
                weekDayCount += DayOfWeek.FRIDAY.getValue() - checkOutDay.getValue() + 1;
            }

            if (isWeekend(returnDay)) {
                weekDayCount += returnDay.getValue();
            }

            long fullWeeks = (daysBetweenDates - weekDayCount) /7;
            weekDayCount += (fullWeeks*5);
        } else {
            for (int i=0; i<daysBetweenDates; i++) {
                DayOfWeek day = DayOfWeek.of(((checkOutDay.getValue() + i) % 7)+1);
                if (!isWeekend(day)) weekDayCount++;
            }
        }
        return weekDayCount;
    }


    private boolean isWeekend(DayOfWeek dayOfWeek) {
        return DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek);
    }

    private void validateCheckout(CheckOut checkOut) throws CheckoutValidationException {
        if (checkOut.getRentalDayCount() < 1) throw new CheckoutValidationException("rentalDayCount", "it must be 1 or greater");
        int discountPercentage = checkOut.getDiscountPercentage();
        if (discountPercentage < 0 || discountPercentage > 100) throw new CheckoutValidationException("discountPercentage", "it must be between 0-100");
    }


}