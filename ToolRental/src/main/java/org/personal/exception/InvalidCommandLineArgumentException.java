package org.personal.exception;


import lombok.Getter;

@Getter
// TODO: NAME PACKAGES CONSISTENTLY
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
