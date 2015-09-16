//
//  WPCSEException.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

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
