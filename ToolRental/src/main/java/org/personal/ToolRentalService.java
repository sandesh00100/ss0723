package org.personal;

import org.apache.commons.cli.*;
import org.personal.enums.Tool;
import org.personal.enums.ToolType;
import org.personal.exceptions.CheckoutValidationException;
import org.personal.exceptions.InvalidCommandLineArgumentException;
import org.personal.models.CheckOut;
import org.personal.models.RentalAgreement;
import org.personal.utility.RentalDateCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class ToolRentalService {
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    public static void main(String ...args) {
        // Set up all the command line options
        Options options = new Options()
                .addOption(Option.builder().option("t").hasArg().longOpt("toolCode").desc("Unique identifier for a tool (eg. 'CHNS' or 'LADW')").required().build())
                .addOption(Option.builder().option("r").hasArg().longOpt("rentalDayCount").desc("Number of days for rental. Any number from 1-100").required().build())
                .addOption(Option.builder().option("d").hasArg().longOpt("discountPercentage").desc("Percentage discounted. Any number from 0-100 (eg. 20 = 20% discount)").required().build())
                .addOption(Option.builder().option("c").hasArg().longOpt("checkoutDate").desc("Date in the MM/DD/YY format. (eg. 07/20/23)").required().build());

        HelpFormatter formatter = new HelpFormatter();
        try {

            // Parse the command line arguments
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(options, args);
            // Use the tool rental service to generate a rental agreement
            ToolRentalService toolRentalService = new ToolRentalService();
            RentalAgreement rentalAgreement = toolRentalService.calculateRentalAgreement(line);
            // Print out the rental agreement
            System.out.println("Rental Agreement");
            System.out.println(rentalAgreement.toString());
        }
        catch (ParseException exp) {
            System.err.println("Parsing failed because " + exp.getMessage());
            formatter.printHelp("ant",options);
            System.exit(-1);
        } catch (InvalidCommandLineArgumentException e) {
            System.err.println("Argument: " + e.getArgumentName() + " is invalid. Please make sure this is in the right format");
            formatter.printHelp("ant",options);
            System.exit(-1);
        } catch (CheckoutValidationException e) {
            System.err.println("Checkout field: " + e.getCheckoutField() + " is not valid because " + e.getReason());
            System.exit(-1);
        }
        // Exit successfully
        System.exit(0);
    }

    /**
     * Calculates a rental agreement from command line arguments
     * @param line command line arguments for rental agreement
     * @return returns a rental agreement
     * @throws InvalidCommandLineArgumentException thrown if command line arguments are not in the right format
     * @throws CheckoutValidationException thrown if there are invalid values for checkout
     */
    public RentalAgreement calculateRentalAgreement(CommandLine line) throws InvalidCommandLineArgumentException, CheckoutValidationException {
        // Make sure it's a valid tool code
        Tool tool = Tool.getToolByCode(line.getOptionValue('t'));
        if (tool == null) throw new InvalidCommandLineArgumentException("toolCode");

        LocalDate checkoutDate;
        int rentalDayCount;
        int discountPercentage;

        // Parse all the command line arguments
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
            checkoutDate = LocalDate.parse(line.getOptionValue('c'), dateFormatter);
        } catch (Exception e) {
           throw new InvalidCommandLineArgumentException("checkOutDate", e);
        }

        // Create a checkout object and calculate rental argument
        CheckOut checkOut = CheckOut.builder()
                .tool(tool)
                .rentalDayCount(rentalDayCount)
                .discountPercentage(discountPercentage)
                .checkOutDate(checkoutDate)
                .build();

        return calculateRentalAgreement(checkOut);
    }

    /**
     * Calculates a rental agreement from the checkout object.
     * @throws CheckoutValidationException thrown if there are invalid values for checkout
     */
    public RentalAgreement calculateRentalAgreement(CheckOut checkOut) throws CheckoutValidationException {
        validateCheckout(checkOut);
        ToolType toolType = checkOut.getTool().getType();
        LocalDate checkoutDate = checkOut.getCheckOutDate();
        LocalDate dueDate = checkoutDate.plusDays(checkOut.getRentalDayCount()-1);
        int chargeDays = getChargableDays(toolType, checkOut.getCheckOutDate(), dueDate);

        // Calculate all monetary values with BigDecimal for accuracy
        BigDecimal preDiscountCharge = toolType.getDailyCharge().multiply(BigDecimal.valueOf(chargeDays));
        BigDecimal discountPercentage = BigDecimal.valueOf((double) checkOut.getDiscountPercentage()/100.0);
        BigDecimal discountAmount = preDiscountCharge.multiply(discountPercentage);
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        // Generate rental agreement
        return RentalAgreement.builder()
                .checkOut(checkOut)
                .chargeDays(chargeDays)
                .dueDate(dueDate)
                .preDiscountCharge(preDiscountCharge)
                .discountAmount(discountAmount)
                .finalCharge(finalCharge)
                .build();
    }

    /**
     * Calculates the chargable days based on tool type and how long it's been checked out for
     * @return
     */
    private int getChargableDays(ToolType toolType, LocalDate checkOutDate, LocalDate dueDate) {
        RentalDateCalculator rentalDateCalculator = new RentalDateCalculator();
        int chargableDays = 0;
        if (toolType.isWeekDayCharge()) {
            // If you can charge it on weekdays, get all the weekdays between the range
            chargableDays += rentalDateCalculator.getWeekDaysBetweenDates(checkOutDate, dueDate);
            if (!toolType.isHolidayCharge()) {
                // If there is no charge on holidays, offset the chargable weekdays
                Set<LocalDate> holidays = rentalDateCalculator.getHolidaysBetweenDates(checkOutDate, dueDate);
                chargableDays -= holidays.size();
            }
        }
        if (toolType.isWeekendCharge()) {
            // If you can charge it on weekends, get all the weekend days between the range
            chargableDays += rentalDateCalculator.getWeekEndDaysBetweenDates(checkOutDate, dueDate);
        }

        return chargableDays;
    }

    /**
     * Validates the checkout object
     * @throws CheckoutValidationException thrown if there are invalid values for checkout
     */
    private void validateCheckout(CheckOut checkOut) throws CheckoutValidationException {
        if (checkOut.getRentalDayCount() < 1) throw new CheckoutValidationException("rentalDayCount", "it must be 1 or greater");
        int discountPercentage = checkOut.getDiscountPercentage();
        if (discountPercentage < 0 || discountPercentage > 100) throw new CheckoutValidationException("discountPercentage", "it must be between 0-100");
    }


}