package utils;

/**
 * Custom exception for Selenium WebDriver operations.
 * Provides better context and error handling for test automation failures.
 */
public class SeleniumActionException extends RuntimeException {
    
    /**
     * Constructs a new SeleniumActionException with the specified detail message.
     * @param message the detail message
     */
    public SeleniumActionException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new SeleniumActionException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public SeleniumActionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new SeleniumActionException with the specified cause.
     * @param cause the cause
     */
    public SeleniumActionException(Throwable cause) {
        super(cause);
    }
} 