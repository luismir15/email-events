package com.example.emailevents.exception;

/**
 * Custom Exception extending the RuntimeException class. This
 * exception will mainly be used if the GET request could not
 * perform properly or if an empty object was to be returned
 */
public class NoSuchElementFoundException extends RuntimeException {

    /**
     * Constructor to set message
     * @param message error
     */
    public NoSuchElementFoundException(String message){
        super(message);
    }
}
