/**
 * File: MalInputException.java
 * Date: December 1997-January 1998
 * Author: Darsono Sutedja
 *
 * This is the exception class that will be generated whenever
 * server sends back an error message
 *
 * Copyright (c) 1997 Darsono Sutedja
 */
public class MalInputException extends Exception {
    //zero-argument constructor...designed for lazy programmer
    public MalInputException() {
        super();
    }
    //constructor that takes caller error message
    public MalInputException(String error) {
        super(error);
        this.error = error;
    }
    //used when caller wants to convert the error to strings
    public String toString() {
        return error;
    }
    //default error message.
    String error="Damn!!!";
}