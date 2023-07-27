package org.demo.exceptions;


import lombok.Getter;

/**
 * This exception is thrown when the app can't parse the invalid command line arguments
 */
@Getter
public class InvalidCommandLineArgumentException extends Exception{
    private final String argumentName;
    public InvalidCommandLineArgumentException(String argumentName) {
        super();
        this.argumentName = argumentName;
    }
    public InvalidCommandLineArgumentException(String argumentName, Throwable e) {
        super(e);
        this.argumentName = argumentName;
    }
}
