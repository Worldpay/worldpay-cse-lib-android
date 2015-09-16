//
//  WPValidationErrorCodes.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse;

import com.worldpay.cse.exception.WPCSEInvalidCardData;

/**
 * Contains the list of error codes mapped to their meanings. Used in conjunction with {@link WPCSEInvalidCardData} exception.
 * <br><br>
 * <pre>
 * //Credit card number is empty. Credit card number is mandatory
 * public static final int EMPTY_CARD_NUMBER = 101;
 *
 * //Invalid credit card number, numbers only and should be between 12 and 20 digits.
 * public static final int INVALID_CARD_NUMBER = 102;
 *
 * //Invalid credit card number, input doesn't verify Luhn check.
 * public static final int INVALID_CARD_NUMBER_BY_LUHN = 103;
 *
 * //Invalid security code, numbers only and should be between 3 and 4 digits.
 * public static final int INVALID_CVC = 102;
 *
 * //Expiry month is empty. Expiry month value is mandatory.
 * public static final int EMPTY_EXPIRY_MONTH = 301;
 *
 * //Invalid expiry month; only numbers expected and in XX form (e.g. 09).
 * public static final int INVALID_EXPIRY_MONTH = 302;
 *
 * //Invalid expiry month, should range from 01 to 12.
 * public static final int INVALID_EXPIRY_MONTH_OUT_RANGE = 303;
 *
 * //Empty expiry year. Expiry year is mandatory.
 * public static final int EMPTY_EXPIRY_YEAR = 304;
 *
 * //Invalid expiry year, only numbers expected.
 * public static final int INVALID_EXPIRY_YEAR = 305;
 *
 * //Invalid expiry date. Expiry date should be in the future.
 * public static final int INVALID_EXPIRY_DATE = 306;
 *
 * //Empty card holder's name. Card holder's name is mandatory.
 * public static final int EMPTY_CARD_HOLDER_NAME = 401;
 *
 * //Invalid card holder's name. Name should not exceed thirty(30) characters.
 * public static final int INVALID_CARD_HOLDER_NAME = 402;
 * </pre>
 */
public class WPValidationErrorCodes {

    /**
     * Credit card number is empty. Credit card number is mandatory
     */
    public static final int EMPTY_CARD_NUMBER = 101;
    /**
     * Invalid credit card number, numbers only and should be between 12 and 20 digits.
     */
    public static final int INVALID_CARD_NUMBER = 102;
    /**
     * Invalid credit card number, input doesn't verify Luhn check.
     */
    public static final int INVALID_CARD_NUMBER_BY_LUHN = 103;
    /**
     * Invalid security code, numbers only and should be between 3 and 4 digits.
     */
    public static final int INVALID_CVC = 201;
    /**
     * Expiry month is empty. Expiry month value is mandatory.
     */
    public static final int EMPTY_EXPIRY_MONTH = 301;
    /**
     * Invalid expiry month; only numbers expected and in XX form (e.g. 09).
     */
    public static final int INVALID_EXPIRY_MONTH = 302;
    /**
     * Invalid expiry month, should range from 01 to 12.
     */
    public static final int INVALID_EXPIRY_MONTH_OUT_RANGE = 303;
    /**
     * Empty expiry year. Expiry year is mandatory.
     */
    public static final int EMPTY_EXPIRY_YEAR = 304;
    /**
     * Invalid expiry year, only numbers expected.
     */
    public static final int INVALID_EXPIRY_YEAR = 305;
    /**
     * Invalid expiry date. Expiry date should be in the future.
     */
    public static final int INVALID_EXPIRY_DATE = 306;
    /**
     * Empty card holder's name. Card holder's name is mandatory.
     */
    public static final int EMPTY_CARD_HOLDER_NAME = 401;
    /**
     * Invalid card holder's name. Name should not exceed thirty(30) characters.
     */
    public static final int INVALID_CARD_HOLDER_NAME = 402;
}
