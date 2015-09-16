//
//  WPPublicKey.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import com.worldpay.cse.exception.WPCSEInvalidPublicKey;

/**
 * Worldpay RSA public key container and parser
 */
public class WPPublicKey {

    private static final String EMPTY = "";
    private static final int HEXADECIMAL = 16;
    private static final String HASH = "#";

    private RSAPublicKey key = null;
    private String keySeqNo = null;

    /**
     * Parses a plain string format as rsa public key. Usage is like below:
     * <pre>
     * WPPublicKey key = WPPublicKey.parseKey("1#10001#bf49edcaba456c6357e4ace484c3fba212543e78bf" +
     *        "72a8c2238caaa1c7ed20262956caa61d74840598d9b0707bc8" +
     *        "2e66f18c8b369c77ae6be0429c93323bb7511fc73d9c7f6988" +
     *        "72a8384370cd77c7516caa25a195d48701e3e0462d61200983" +
     *        "ba26cc4a20bb059d5beda09270ea6dcf15dd92084c4d5867b6" +
     *        "0986151717a8022e4054462ee74ab8533dda77cee227a49fda" +
     *        "f58eaeb95df90cb8c05ee81f58bec95339b6262633aef216f3" +
     *        "ae503e8be0650350c48859eef406e63d4399994b147e45aaa1" +
     *        "4cf9936ac6fdd7d4ec5e66b527d041750ba63a8296b3e6e774" +
     *        "a02ee6025c6ee66ef54c3688e4844be8951a8435e6b6e8d676" +
     *        "3d9ee5f16521577e159d");
     * </pre>
     * @param plainKey The plain public key
     * @throws WPCSEInvalidPublicKey
     */
    public static WPPublicKey parseKey(String plainKey) throws WPCSEInvalidPublicKey {
        try {
            String[] components = plainKey.split(HASH);
            if (components.length < 3) {
                throw new WPCSEInvalidPublicKey();
            }
            String exponent = components[1];
            String modulus = components[2];
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(modulus, HEXADECIMAL), new BigInteger(exponent, HEXADECIMAL));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey key = (RSAPublicKey)keyFactory.generatePublic(keySpec);
            String keySeqNo = components[0];
            if (EMPTY.equals(keySeqNo)) {
                throw new WPCSEInvalidPublicKey();
            }
            return new WPPublicKey(key, keySeqNo);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NumberFormatException e) {
            throw new WPCSEInvalidPublicKey(e);
        }
    }

    /**
     * Create a WPPublicKey object based on standard Java interface {@link RSAPublicKey} and key sequence number
     * @param key a RSAPublicKey instance
     * @param keySeqNo the key sequence number
     */
    public WPPublicKey(RSAPublicKey key, String keySeqNo) {
        this.key = key;
        this.keySeqNo = keySeqNo;
    }

    /**
     * Returns a RSAPublicKey instance created based on the provided plain text public key
     *
     * @return the RSAPublicKey instance
     */
    public RSAPublicKey getKey() {
        return key;
    }

    /**
     * Return the key sequence number with which the public key was initialised
     *
     * @return a string that represents a number.
     */
    public String getKeySeqNo() {
        return keySeqNo;
    }

    /**
     * Return the public key in raw/plain format using Worldpay standard
     *
     * @return a string that represents the public key
     */
    public String toString() {
        return keySeqNo + HASH + key.getPublicExponent().toString(HEXADECIMAL) + HASH + key.getModulus().toString(HEXADECIMAL);
    }
}
