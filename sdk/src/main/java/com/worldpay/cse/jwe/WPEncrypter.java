//
//  WPEncrypter.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse.jwe;

/**
 * Encrypter interface. Implemented by {@link WPRSAEncrypter} and {@link WPAESEncrypter}
 */
interface WPEncrypter {

    /**
     * Encrypts the <code>data</code>
     *
     * @param data the plain data as an array of bytes
     * @return the encrypted data (cipher) as an array of bytes
     */
    byte[] encrypt(byte[] data);
}
