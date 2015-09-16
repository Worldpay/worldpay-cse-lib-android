//
//  WPCSEInvalidCardData.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse.exception;

import java.util.Set;

/**
 * The exception that contains the error codes for the invalid values of the card fields.
 * The list of error codes can be retrieved via {@link WPCSEInvalidCardData#getErrorCodes()}
 */
public class WPCSEInvalidCardData extends WPCSEException {

    public static final String INVALID_CARD_DATA = "Invalid card data";
    private final Set<Integer> errorCodes;

    /**
     * Creates an exception for Invalid card data with the list of error codes. Each code represent a kind of error for a particular card field
     *
     * @param errorCodes the list of error codes.
     *
     * @see com.worldpay.cse.WPCardValidator
     */
    public WPCSEInvalidCardData(Set<Integer> errorCodes) {
        super(INVALID_CARD_DATA);
        this.errorCodes = errorCodes;
    }

    /**
     * Returns the list of error codes. The following is the list of error codes mapped to their meanings which
     * would be provided back to the caller app. All these codes are mapped to {@link com.worldpay.cse.WPValidationErrorCodes}
     <ul>
     •	101 - Credit card number is empty. <br>
     •	102 - Invalid credit card number, numbers only and should be between 12 and 20 digits.<br>
     •	103 - Invalid credit card number, input doesn't verify Luhn check.<br>
     •	201 - Invalid security code, numbers only and should be between 3 and 4 digits.<br>
     •	301 - Expiry month is empty.<br>
     •	302 - Invalid expiry month; only numbers expected and in XX form (e.g. 09).<br>
     •	303 - Invalid expiry month, should range from 01 to 12.<br>
     •	304 - Expiry year is mandatory.<br>
     •	305 - Invalid expiry year, only numbers expected.<br>
     •	306 - Expiry date is not in the future.<br>
     •	401 - Card holder's name is empty.<br>
     •	402 - Card holder's name exceeds thirty characters.<br>
     </ul>
     *  @see com.worldpay.cse.WPValidationErrorCodes
     *
     * @return a set of integers, which are the error codes
     */
    public Set<Integer> getErrorCodes() {
        return errorCodes;
    }
}
