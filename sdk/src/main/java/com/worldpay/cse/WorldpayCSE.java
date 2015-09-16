//
//  WorldpayCSE.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse;

import com.worldpay.cse.jwe.WPJWEObject;
import com.worldpay.cse.jwe.WPJWEHeader;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.worldpay.cse.exception.WPCSEException;
import com.worldpay.cse.exception.WPCSEInvalidCardData;
import com.worldpay.cse.exception.WPCSEInvalidPublicKey;

/**
 * The main entry point class of the Worldpay CSE(Client Side Encryption) SDK.
 *
 * This class is used to encrypt the provided {@link WPCardData} payload.
 * <p>
 *     Example usage:
 * </p>
 * <pre>
 *
 *  WPCardData cardData = new WPCardData();
 *  cardData.setCardHolderName("John Smith");
 *  cardData.setCardNumber("444433332221111");
 *  cardData.setCvc("123");
 *  cardData.setExpiryMonth("11");
 *  cardData.setExpiryYear("2020");
 *
 *  String publicKey = "1#10001#121ad121...";// check {@link WorldpayCSE#setPublicKey(String)} for more details
 *  String encryptedData = "";
 *
 *  WorldpayCSE worldPayCSE = new WorldpayCSE();
 *  try {
 *      worldPayCSE.setPublicKey(publicKey);
 *      encryptedData = worldPayCSE.encrypt(cardData);
 *  } catch (WPCSEException e) {
 *     //show error message
 *  }

 * </pre>
 *
 */
public class WorldpayCSE {

    public static final String RSA_1_5 = "RSA1_5";
    public static final String A_256_GCM = "A256GCM";
    public static final String API_VERSION = "1.0";
    public static final String LIB_VERSION = "1.0.1";
    public static final String CHANNEL = "android";

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private WPPublicKey publicKey;

    /**
     * Creates a WorldpayCSE object.
     */
    public WorldpayCSE() {
        super();
    }

    /**
     * Sets the public(RSA) key that will be used for any future {@link WorldpayCSE#encrypt(WPCardData)} calls.
     * <br>
     * The key must be provided in the Worldpay format:
     * <pre>sequence#exponent#moduls</pre>
     * Example:
     * <pre>
     *"1#10001#bf49edcaba456c6357e4ace484c3fba212543e78bf
     * 72a8c2238caaa1c7ed20262956caa61d74840598d9b0707bc8
     * 2e66f18c8b369c77ae6be0429c93323bb7511fc73d9c7f6988
     * 72a8384370cd77c7516caa25a195d48701e3e0462d61200983
     * ba26cc4a20bb059d5beda09270ea6dcf15dd92084c4d5867b6
     * 0986151717a8022e4054462ee74ab8533dda77cee227a49fda
     * f58eaeb95df90cb8c05ee81f58bec95339b6262633aef216f3
     * ae503e8be0650350c48859eef406e63d4399994b147e45aaa1
     * 4cf9936ac6fdd7d4ec5e66b527d041750ba63a8296b3e6e774
     * a02ee6025c6ee66ef54c3688e4844be8951a8435e6b6e8d676
     * 3d9ee5f16521577e159d"
     * </pre>
     *
     * @param  publicKey the public key in Worldpay text format.
     * @throws WPCSEInvalidPublicKey if the public key does not adhere to the Worldpay format.
     */
    public void setPublicKey(String publicKey) throws WPCSEInvalidPublicKey {
        this.publicKey = WPPublicKey.parseKey(publicKey);
    }

    /**
     * Returns the current public(RSA) key in string format
     *
     * @return a {@link WPPublicKey} instance
     */
    public WPPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the public(RSA) key that will be used for any future {@link WorldpayCSE#encrypt(WPCardData)} calls.
     *
     * @param  publicKey a {@link WPPublicKey} instance
     */
    public void setPublicKey(WPPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Validates in a more convenient way if the public key is valid. May be useful before calling {@link WorldpayCSE#setPublicKey(String)}
     * method and you do not want to handle WPCSEInvalidPublicKey exception.
     *
     * @param stringKey the public key in plain string format
     * @return true if valid
     */
    public static boolean isValidPublicKey(String stringKey) {
        try {
            WPPublicKey.parseKey(stringKey);
        } catch (WPCSEInvalidPublicKey e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Validates and encrypts the supplied card data. If any validation error occurs, it will throw a
     * {@link WPCSEInvalidCardData} exception that contains all error codes.
     *
     * @param cardData An object containing information about the card details to encrypt.
     * @return The encrypted data to be submitted for processing, or null if a validation error occurs.
     *
     * @throws WPCSEException could be an instance of {@link WPCSEInvalidCardData} or other generic instance.
     */
    public String encrypt(WPCardData cardData) throws WPCSEException {

        Set<Integer> errors = new WPCardValidator().validateCardData(cardData);
        if (errors.isEmpty()) {
            if (publicKey != null) {
                return performEncryption(cardData.toString());
            } else {
                throw new WPCSEException("Public key not set");
            }
        } else {
            throw new WPCSEInvalidCardData(errors);
        }
    }

    /**
     * Validates the payment card field values and returns the list of error codes.
     *
     * @param data the {@link WPCardData} object
     * @return the error codes
     *
     * @see WPValidationErrorCodes
     */
    public static Set<Integer> validate(WPCardData data) {
        return new WPCardValidator().validateCardData(data);
    }

    private String performEncryption(String data) {

        WPJWEHeader header = new WPJWEHeader();
        header.setAlgorithm(RSA_1_5);
        header.setEncryption(A_256_GCM);
        header.setKid(publicKey.getKeySeqNo());
        header.setApiVersion(API_VERSION);
        header.setLibVersion(LIB_VERSION);
        header.setChannel(CHANNEL);

        WPJWEObject jweObject = new WPJWEObject(header, data);
        jweObject.setKey(publicKey.getKey());
        jweObject.encrypt();
        return jweObject.serialize();

    }

}
