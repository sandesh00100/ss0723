package org.personal;

import lombok.Value;
import org.apache.commons.cli.*;
import org.personal.enums.Tool;
import org.personal.exceptions.CheckoutValidationException;
import org.personal.exceptions.InvalidCommandLineArgumentException;
import org.personal.models.CheckOut;
import org.personal.models.RentalAgreement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.time.LocalDate;

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

    private void validateCheckout(CheckOut checkOut) throws CheckoutValidationException {
        if (checkOut.getRentalDayCount() < 1) throw new CheckoutValidationException("rentalDayCount", "it must be 1 or greater");
        int discountPercentage = checkOut.getDiscountPercentage();
        if (discountPercentage < 0 || discountPercentage > 100) throw new CheckoutValidationException("discountPercentage", "it must be between 0-100");
    }
    public RentalAgreement calculateRentalAgreement(CheckOut checkOut) throws CheckoutValidationException {
        validateCheckout(checkOut);
        return null;
    }
}