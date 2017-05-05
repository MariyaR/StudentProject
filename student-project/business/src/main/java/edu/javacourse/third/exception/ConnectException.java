package edu.javacourse.third.exception;

/**
 * Created by antonsaburov on 09.03.17.
 */
public class ConnectException extends Exception
{
    public ConnectException(String message) {
        super(message);
    }

    public ConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
