package org.personal;

import com.beust.jcommander.JCommander;
import org.personal.args.CheckoutArgs;

public class ToolRentalApp {
    public static void main(String[] args) {
        CheckoutArgs checkoutArgs = new CheckoutArgs();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(checkoutArgs)
                .build();

        jCommander.parse(args);
        jCommander.usage();
        System.out.println(checkoutArgs.getCheckoutDate());
    }
}