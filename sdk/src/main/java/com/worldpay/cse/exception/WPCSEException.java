package com.worldpay.cse.exception;

/**
 * Generic exception for WorldpayCSE SDK
 */
public class WPCSEException extends RuntimeException {

    public WPCSEException(String displayMessage) {
        super(displayMessage);
    }

    public WPCSEException(String displayMessage, Throwable cause) {
        super(displayMessage, cause);
    }
}
