package org.demo.exceptions;

import lombok.Getter;

/**
 * This exception is thrown when user's checkout fails validation
 */
@Getter
public class CheckoutValidationException extends Exception{
    private final String checkoutField;
    private final String reason;
    public CheckoutValidationException(String checkoutField, String  reason){
        super();
        this.checkoutField = checkoutField;
        this.reason =  reason;
    }
}
