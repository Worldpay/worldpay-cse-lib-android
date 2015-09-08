package com.worldpay.cse.exception;

/**
 * Invalid RSA public key exception
 */
public class WPCSEInvalidPublicKey extends WPCSEException {

    public static final String INVALID_PUBLIC_KEY = "Invalid public key";

    public WPCSEInvalidPublicKey() {
        super(INVALID_PUBLIC_KEY);
    }

    public WPCSEInvalidPublicKey(Throwable cause) {
        super(INVALID_PUBLIC_KEY, cause);
    }
}
