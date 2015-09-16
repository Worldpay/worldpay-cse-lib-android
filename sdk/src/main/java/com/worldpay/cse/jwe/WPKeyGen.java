//
//  WPKeyGen.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse.jwe;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Key generator class
 *
 */
class WPKeyGen {

    private WPKeyGen() {
    }

    /**
     * The standard Initialisation Vector (IV) length (96 bits).
     */
    public static final int IV_BIT_LENGTH = 96;
    /**
     * The standard authentication tag length (128 bits).
     */
    public static final int AUTH_TAG_BIT_LENGTH = 128;
    /**
     * The standard key length (256 bits).
     */
    public static final int KEY_BIT_LENGTH = 256;

    private static Random randomGen = new SecureRandom();

    /**
     * Generate a key based on provided key size in bits
     *
     * @param keySize the size in bits
     * @return a byte array that represents they key
     */
    public static byte[] generateKey(int keySize) {
        byte[] bytes = new byte[keySize / 8];
        randomGen.nextBytes(bytes);
        return bytes;
    }

}
