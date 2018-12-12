//
//  WPCardValidator.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Card data field validator
 *
 The following is list of error codes mapped to their meanings which would be provided back to the caller application.
 <ul>
 •	101 - Credit card number is mandatory. <br>
 •	102 - Enter a valid credit card number, numbers only and should be between 12 and 20 digits.<br>
 •	103 - Enter a valid credit card number, input doesn't verify Luhn check.<br>
 •	201 - Enter a valid security code, numbers only and should be between 3 and 4 digits.<br>
 •	301 - Expiry month is mandatory.<br>
 •	302 - Enter a valid expiry month; only numbers expected and in XX form (e.g. 09).<br>
 •	303 - Enter a valid expiry month, should range from 01 to 12.<br>
 •	304 - Expiry year is mandatory.<br>
 •	305 - Enter a valid expiry year, only numbers expected.<br>
 •	306 - Expiry date should be in future.<br>
 •	401 - Card holder's name is mandatory.<br>
 •	402 - Name should not exceed thirty characters.<br>
 </ul>
 *
 */
class WPCardValidator {

    private static final String EX_MONTH_PATTERN = "^[0-9]{2}$";
    private static final String EX_YEAR_PATTERN = "^[0-9]{4}$";
    private static final String CARD_NUMBER_PATTERN = "[0-9]{12,20}";
    private static final String CARD_HOLDER_PATTERN = "^.{1,30}$";
    private static final String CVC_PATTERN = "^[0-9]{3,4}$";

    private static final int MAX_SINGLE_DIGIT_NUMBER = 9;
    private static final int DIVIDER_VALUE = 10;
    private static final int AMPLIFIER_VALUE = 2;

    private static final int INVALID_MONTH = 300;
    private static final int INVALID_MONTH_2 = 303;
    private static final int INVALID_YEAR = 303;
    private static final int INVALID_DATE = 306;
    private static final int INVALID_CARD_HOLDER_NAME = 400;
    private static final int INVALID_CVC = 201;
    private static final int INVALID_CARD_NUMBER = 100;
    private static final int INVALID_LUHN = 103;

    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;
    private static final int EMPTY_VALUE = 1;
    private static final int INVALID_VALUE = 2;
    private static final int NO_ERROR = 0;

    /**
     * Validates card data.
     *
     * @param cardData the card data object
     * @return the list of error codes
     */
    public Set<Integer> validateCardData(WPCardData cardData) {
        Set<Integer> errorsCodes = new HashSet<>();
        checkErrorCode(validateCardNumber(cardData.getCardNumber()), errorsCodes);
        checkErrorCode(validateCvc(cardData.getCvc()), errorsCodes);
        checkErrorCode(validateMonth(cardData.getExpiryMonth()), errorsCodes);
        checkErrorCode(validateYear(cardData.getExpiryYear()), errorsCodes);
        checkErrorCode(validateDate(cardData.getExpiryMonth(), cardData.getExpiryYear()), errorsCodes);
        checkErrorCode(validateCardHolderName(cardData.getCardHolderName()), errorsCodes);
        return errorsCodes;
    }

    private int validateMonth(String expiryMonth) {
        int validNumber = validateValue(expiryMonth, EX_MONTH_PATTERN);
        if (validNumber != NO_ERROR) {
            return INVALID_MONTH + validNumber;
        }
        int exMonth = Integer.parseInt(expiryMonth);
        if (exMonth < MIN_MONTH || exMonth > MAX_MONTH) {
            return INVALID_MONTH_2;
        }
        return NO_ERROR;
    }

    private int validateYear(String expiryYear) {
        int validNumber = validateValue(expiryYear, EX_YEAR_PATTERN);
        if (validNumber != NO_ERROR) {
            return INVALID_YEAR + validNumber;
        }
        return NO_ERROR;
    }

    private int validateDate(String expiryMonth, String expiryYear) {
        if (validateMonth(expiryMonth) == NO_ERROR && validateYear(expiryYear) == NO_ERROR) {
            int exMonth = Integer.parseInt(expiryMonth);
            int exYear = Integer.parseInt(expiryYear);
            if (!isFutureDate(exMonth, exYear)) {
                return INVALID_DATE;
            }
        }
        return NO_ERROR;
    }

    private int validateValue(String value, String pattern) {
        if (isEmpty(value) || isEmpty(value.trim())) {
            return EMPTY_VALUE;
        }
        if(!evaluateRegex(value, pattern)) {
            return INVALID_VALUE;
        }
        return NO_ERROR;
    }

    private boolean isFutureDate(int expiryMonth, int expiryYear) {
        Calendar now = Calendar.getInstance();
        int currentMonth = now.get(Calendar.MONTH);
        int currentYear = now.get(Calendar.YEAR);
        return (expiryYear * 12) + (expiryMonth - 1) >= (currentYear * 12) + currentMonth;
    }

    private int validateCardHolderName(String cardHolderName) {
        int valid = validateValue(cardHolderName, CARD_HOLDER_PATTERN);
        if (valid != NO_ERROR) {
            return INVALID_CARD_HOLDER_NAME + valid;
        }
        return NO_ERROR;
    }

    private int validateCvc(String cvc) {
        if (isEmpty(cvc) || evaluateRegex(cvc, CVC_PATTERN)) {
            return NO_ERROR;
        } else {
            return INVALID_CVC;
        }
    }

    private int validateCardNumber(String cardNumber) {
        int valid = validateValue(cardNumber, CARD_NUMBER_PATTERN);
        if (valid != NO_ERROR) {
            return INVALID_CARD_NUMBER + valid;
        } else if (!validateLuhn(cardNumber)) {
            return INVALID_LUHN;
        }
        return NO_ERROR;
    }

    public boolean validateLuhn(String value) {

        int nCheck = 0;
        boolean alternate = false;

        for (int i = value.length() - 1; i >= 0; i--) {
            int nDigit = Integer.parseInt(String.valueOf(value.charAt(i)));

            if (alternate) {
                nDigit *= AMPLIFIER_VALUE;
                if (nDigit > MAX_SINGLE_DIGIT_NUMBER) {
                    nDigit -= MAX_SINGLE_DIGIT_NUMBER;
                }
            }
            nCheck += nDigit;
            alternate = !alternate;

        }
        return (nCheck % DIVIDER_VALUE) == 0;
    }

    private boolean checkErrorCode(int errorCode, Set<Integer> errorsCodes) {
        if (errorCode != NO_ERROR) {
            errorsCodes.add(errorCode);
            return false;
        } else {
            return true;
        }
    }

    private boolean evaluateRegex(String data, String re) {
        return Pattern.matches(re, data);
    }

    private boolean isEmpty(String data) {
        return (data == null) || "".equals(data) || evaluateRegex(data, "!/[^\\s]/");
    }

}
