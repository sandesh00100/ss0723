package org.personal.exception;

import lombok.Getter;

@Getter
public class CheckoutValidationException extends Exception{
    private final String checkoutField;
    private final String validationFailure;
    public CheckoutValidationException(String checkoutField, String validationFailure){
        super();
        this.checkoutField = checkoutField;
        this.validationFailure = validationFailure;
    }
}
