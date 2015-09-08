package com.worldpay.cse;

import com.google.gson.Gson;

/**
 * Payment Card data object that holds the sensitive fields:
 * <ul>
 * <li> cardHolderName - The name of the card holder.</li>
 * <li> cardNumber - The card's PAN.</li>
 * <li> expiryMonth - The 2 digit month of the card's expiry date.</li>
 * <li> expiryYear - The 4 digit year of the card's expiry date.</li>
 * <li> cvc - The optional CVC for the card.</li>
 *
 * </ul>
 */
public class WPCardData {

    private String cardNumber;
    private String cvc;
    private String expiryMonth;
    private String expiryYear;
    private String cardHolderName;

    public WPCardData() {
        super();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public static WPCardData parseJSON(String json) {
        return new Gson().fromJson(json, WPCardData.class);
    }
}
