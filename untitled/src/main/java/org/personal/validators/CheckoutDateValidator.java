package org.personal.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.time.format.DateTimeFormatter;

public class CheckoutDateValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YY");
        formatter.parse(value);
    }
}
