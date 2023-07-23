package org.personal.args;

import com.beust.jcommander.Parameter;
import lombok.Data;
import lombok.Value;

@Data
public class CheckoutArgs {
    @Parameter(names = {"--toolCode", "-t"}, description = "Unique identifier for a tool (eg. 'CHNS' or 'LADW')")

    private String toolCode;

    @Parameter(names = {"--rentalDayCount", "-r"}, description = "Number of days for rental. Any number from 1-100")
    private Integer rentalDayCount;

    @Parameter(names = {"--discountPercentage", "-d",}, description = "Percentage discounted. Any number from 0-100 (eg. 20 = 20% discount)")
    private Integer discountPercentage;

    @Parameter(names = {"--checkoutDate", "-c"}, description = "Date in the MM/DD/YY format. (eg. 07/20/23)")
    private String checkoutDate;


    @Parameter(names = "--help", help = true)
    private boolean help;
}
