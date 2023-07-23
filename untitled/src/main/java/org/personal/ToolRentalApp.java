package org.personal;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.Parameterized;
import lombok.Value;
import org.personal.args.CheckoutArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.Tool;
import java.util.Arrays;
import java.util.Map;

@Value
public class ToolRentalApp {
    private final CheckoutArgs checkoutArgs;
    public static void main(String ...args) {
        Logger logger = LoggerFactory.getLogger(ToolRentalApp.class);

        CheckoutArgs checkoutArgs = new CheckoutArgs();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(checkoutArgs)
                .build();

        logger.debug("Arguments entered " + Arrays.toString(args));

        if (args.length == 0) jCommander.usage();
        else jCommander.parse(args);

        if (jCommander.getFields().isEmpty()) {
            jCommander.usage();
        } else {
            logger.debug("Looping through commands");
        }

        ToolRentalApp toolRentalApp = new ToolRentalApp(checkoutArgs);
    }



}